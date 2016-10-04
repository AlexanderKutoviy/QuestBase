package com.questbase.app.profile;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.questbase.app.R;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.profile.ProfileController;
import com.questbase.app.controller.transaction.TransactionController;
import com.questbase.app.dao.auth.AuthDao;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.ProfileResponse;
import com.questbase.app.domain.UserPhoneDto;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.screens.LoginScreen;
import com.questbase.app.net.body.PayoutRequest;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;
import com.questbase.app.presentation.LogoutPresenter;
import com.questbase.app.profile.popups.CashoutDialog;
import com.questbase.app.sync.SyncUtils;
import com.questbase.app.utils.RespoSchedulers;
import com.questbase.app.utils.TimeUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;

public class PersonalCabPresenter implements LogoutPresenter {
    private static final float MIN_AMOUNT_OF_OUT_MONEY = 50f;
    private static final int RESPOS_COEF = 10;
    private static final int PRIMARY = 1;
    private static final String PHONE_TARGET = "phone";
    private static final String ARMY_TARGET = "4army";
    private static final String HAPPYPAW_TARGET = "happypaw";
    private static final int PHONE_MAX_SIZE = 18;
    private final AuthDao authDao;
    private final FilesController filesController;
    private final ProfileController profileController;
    private final TransactionController transactionController;
    private Router router;
    private PersonalCabView profileView;
    private Optional<CashoutDialog.CharityTarget> charityTarget;
    private Subscription subscription;
    private Float moneyUAN;
    private static PayoutRequest payoutRequest;
    private Resources resources;

    public enum Keys {
        GEN_RESEARCH("genResearchFormAvg"),
        USERS_RESEARCH("usersResearchFormAvg"),
        GEN_FUN("genFunFormAvg"),
        USERS_FUN("usersFunFormAvg");

        Keys(String value) {
            this.value = value;
        }

        public final String value;
    }

    public PersonalCabPresenter(AuthDao authDao, FilesController filesController,
                                ProfileController profileController, TransactionController transactionController) {
        this.authDao = authDao;
        this.filesController = filesController;
        this.profileController = profileController;
        this.transactionController = transactionController;
        charityTarget = Optional.of(CashoutDialog.CharityTarget.PETS);
    }

    void attachView(PersonalCabView view, Router router, Resources resources) {
        this.profileView = view;
        this.router = router;
        this.resources = resources;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        subscription = profileController.observe()
                .observeOn(RespoSchedulers.main())
                .subscribe(profileResponseEvent -> {
                    displayProfileResponse(profileResponseEvent.data);
                });
        profileController.getProfileForms()
                .ifPresent(this::displayPassedForms);
    }

    void detachView() {
        profileView = null;
        router = null;
        subscription.unsubscribe();
    }

    private void displayProfileResponse(ProfileResponse profileResponse) {
        displayChartsValues(profileResponse);
        displayCharityTransactionsAmount(profileResponse);
        displayTransactionsAmount(profileResponse);
        displayBalance(profileResponse);
        displayFormsInProcessAmount(profileResponse);
//        displayConfigs(profileResponse);
        displayTimeOfLastTransaction(profileResponse);
        displayAvatarBitmap();
    }

    private void displayAvatarBitmap() {
        File avatar = filesController.getUsersAvatarPath();
        if (!avatar.exists()) {
            return;
        }
        profileView.renderAvatar(BitmapFactory.decodeFile(avatar.getAbsolutePath()));
    }

    public Bitmap displayFormAvatarBitmap(Form form) {
        File formResourcesPath = new File(filesController.getBasePath(), String.valueOf(form.formId));
        File avatarPath = new File(formResourcesPath, Stream.of(form.resources)
                .filter(resource -> resource.resId.contains("avatar"))
                .findFirst().get().resId);
        if (!avatarPath.exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(avatarPath.getAbsolutePath());
    }

    public void displayConfigs(ProfileResponse profileResponse) {
        if (!profileResponse.emails.isEmpty()) {
            profileView.renderListOfEmails(profileResponse.emails);
        }
        if (!profileResponse.phones.isEmpty()) {
            profileView.renderListOfPhones(profileResponse.phones);
        }
    }

    public void onGetMoneyButtonPressed() {
        profileView.showPerformTransactionDialog();
        profileView.showMessageWithUserBalance(profileController.getProfile().get().balance);
    }

    public void onTransactionHistoryButtonPressed() {
        profileView.showTransactionHistoryDialog(transactionController.getTransactions());
    }

    public void onApprovePerformTransactionSelected(String enteredMoney, Object selectedItem) {
        String phone = getPrimaryPhone().phone;
        moneyUAN = Float.valueOf(enteredMoney) / RESPOS_COEF;
        String onCharity = resources.getStringArray(R.array.spinner_items)[0];
        if (selectedItem.equals(onCharity)) {
            profileView.createConfirmationCharityPopup(showSelectedCharityTarget());
        } else {
            if (phone != null) {
                profileView.createCellConfirmationDialog();
                profileView.showMessageWithUserPhone(phone, moneyUAN);
            } else {
                profileView.createPhoneConfirmationDialog(moneyUAN);
            }
        }
    }

    public void onApproveCellConfirmationDialog() {
        payoutRequest = new PayoutRequest(getPrimaryPhone().phone, PHONE_TARGET,
                String.valueOf((moneyUAN * RESPOS_COEF)));
        profileController.performPayout(payoutRequest);
        callProfileSync();
        profileView.createConfirmationPhonePopup(getPrimaryPhone().phone);
    }

    public void onEnteredMoneyChanged(String enteredMoney, String selectedTarget) {
        profileView.setCanApprove(calculateCanApprove(enteredMoney, selectedTarget));
    }

    public void onCharityTargetSelected(Optional<CashoutDialog.CharityTarget> charityTarget, String enteredMoney) {
        this.charityTarget = charityTarget;
        String onCharity = resources.getStringArray(R.array.spinner_items)[0];
        profileView.setCanApprove(calculateCanApprove(enteredMoney, onCharity));
    }

    public void onTargetChange(String selectedTarget, String enteredMoney) {
        profileView.setCanApprove(calculateCanApprove(enteredMoney, selectedTarget));
    }

    public void onPhoneChange(String phone) {
        profileView.setCanApprovePhone(phone.replaceAll("[\\.+]", "").trim().length() == PHONE_MAX_SIZE);
    }

    public List<TransactionEventsContainerDto> getTransactions() {
        return transactionController.getTransactions();
    }

    @Override
    public void onLogout() {
        authDao.logout();
        router.goTo(new LoginScreen());
    }

    private void callProfileSync() {
        profileController.sync()
                .subscribeOn(RespoSchedulers.io())
                .observeOn(RespoSchedulers.main())
                .subscribe(syncResponse -> Log.d(SyncUtils.SYNC_TAG, "profile synced"));
        transactionController.sync();
    }

    private void displayBalance(ProfileResponse profileResponse) {
        profileView.renderBalance(String.valueOf(profileResponse.balance));
    }

    private void displayFormsInProcessAmount(ProfileResponse profileResponse) {
        profileView.renderAmountOfFormsInProcess(String.valueOf(profileResponse.processFormCount));
    }

    private void displayPassedForms(List<Form> listOfUsersForms) {
        if (listOfUsersForms.isEmpty()) {
            profileView.noRenderFormsBlock();
        }
        profileView.renderListOfPassedForms(listOfUsersForms);
    }

    private void displayTimeOfLastTransaction(ProfileResponse profileResponse) {
        if (!profileResponse.transactionsCount.equals("0")) {
            profileView.renderTimeOfLastTransaction(TimeUtils
                    .formatTime(profileResponse.lastTransactionTime.getTime()));
        }
    }

    private void displayTransactionsAmount(ProfileResponse profileResponse) {
        profileView.renderAmountOfTransactions(profileResponse.transactionsCount);
    }

    private void displayCharityTransactionsAmount(ProfileResponse profileResponse) {
        profileView.renderAmountOfCharityTransactions(
                String.valueOf(profileResponse.charityTransactionsCount));
    }

    private void displayChartsValues(ProfileResponse profileResponse) {
        Map<String, Float> valuesForCharts = new HashMap<>();
        valuesForCharts.put(Keys.GEN_RESEARCH.value,
                Float.parseFloat(profileResponse.researchFormsAvg));
        valuesForCharts.put(Keys.USERS_RESEARCH.value,
                Float.parseFloat(profileResponse.currentResearchFormsCount));
        valuesForCharts.put(Keys.GEN_FUN.value,
                Float.parseFloat(profileResponse.funFormsAvg));
        valuesForCharts.put(Keys.USERS_FUN.value,
                Float.parseFloat(profileResponse.currentFunFormsCount));
        profileView.renderPassedFormsGraphs(valuesForCharts);
    }

    private boolean calculateCanApprove(String enteredMoney, String selectedTarget) {
        String onCharity = resources.getStringArray(R.array.spinner_items)[0];
        if (selectedTarget.equals(onCharity)) {
            return (charityTarget.isPresent() && isMoneyEnough(enteredMoney));
        } else {
            return isMoneyEnough(enteredMoney);
        }
    }

    private boolean isMoneyEnough(String enteredMoney) {
        return !(enteredMoney.isEmpty()
                || Float.valueOf(enteredMoney) > profileController.getProfile().get().balance
                || Float.valueOf(enteredMoney) < MIN_AMOUNT_OF_OUT_MONEY);
    }

    private String showSelectedCharityTarget() {
        if (charityTarget.get().toString().equals(CashoutDialog.CharityTarget.ARMY.name())) {
            payoutRequest = new PayoutRequest(getPrimaryPhone().phone, ARMY_TARGET,
                    String.valueOf((moneyUAN * RESPOS_COEF)));
            profileController.performPayout(payoutRequest);
            callProfileSync();
            return resources.getString(R.string.charity_folk_rear);
        } else {
            payoutRequest = new PayoutRequest(getPrimaryPhone().phone, HAPPYPAW_TARGET,
                    String.valueOf((moneyUAN * RESPOS_COEF)));
            profileController.performPayout(payoutRequest);
            callProfileSync();
            return resources.getString(R.string.happy_paw);
        }
    }

    private UserPhoneDto getPrimaryPhone() {
        List<UserPhoneDto> usersPhones = profileController.getProfile().get().phones;
        return Stream.of(usersPhones).filter(phone -> isPrimary(phone.isPrimary))
                .findFirst()
                .orElse(new UserPhoneDto());
    }

    private boolean isPrimary(int primary) {
        return primary == PRIMARY;
    }
}