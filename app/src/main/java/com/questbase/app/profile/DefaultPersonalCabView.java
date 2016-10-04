package com.questbase.app.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.domain.Form;
import com.questbase.app.domain.UserEmailDto;
import com.questbase.app.domain.UserPhoneDto;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.flowui.drawer.RespoDrawerView;
import com.questbase.app.net.entity.transactions.TransactionEventsContainerDto;
import com.questbase.app.profile.popups.CashoutDialog;
import com.questbase.app.profile.popups.CellConfirmationDialog;
import com.questbase.app.profile.popups.ConfirmationDialog;
import com.questbase.app.profile.popups.PhoneConfirmationDialog;
import com.questbase.app.profile.popups.TransactionDialog;
import com.questbase.app.utils.ChartUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import lecho.lib.hellocharts.view.ColumnChartView;

public class DefaultPersonalCabView extends DrawerLayout implements PersonalCabView, RouterOwner {
    private TransactionDialog transactionDialog;
    private CashoutDialog cashoutDialog;
    private CellConfirmationDialog cellConfirmationDialog;
    private ConfirmationDialog confirmationDialog;
    private PhoneConfirmationDialog phoneConfirmationDialog;

    @Inject
    PersonalCabPresenter personalCabPresenter;
    private Router router;

    public DefaultPersonalCabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerDefaultPersonalCabView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.menu_item).setOnClickListener(v ->
                ((RespoDrawerView) findViewById(R.id.left_drawer)).drawerLayout.openDrawer(Gravity.LEFT)
        );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        personalCabPresenter.attachView(this, router, getResources());
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, personalCabPresenter);
        findViewById(R.id.get_money_button).setOnClickListener(view ->
                personalCabPresenter.onGetMoneyButtonPressed());
        if (!personalCabPresenter.getTransactions().isEmpty()) {
            findViewById(R.id.show_all_transactions_button).setOnClickListener(view ->
                    personalCabPresenter.onTransactionHistoryButtonPressed());
        } else {
            findViewById(R.id.show_all_transactions_button).setClickable(false);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        personalCabPresenter.detachView();
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
        ((RespoDrawerView) findViewById(R.id.left_drawer)).init(this, router, personalCabPresenter);
    }

    @Override
    public void renderAvatar(Bitmap img) {
        ((ImageView) findViewById(R.id.header_avatar)).setImageBitmap(img);
    }

    @Override
    public void renderPassedFormsGraphs(Map<String, Float> values) {
        ChartUtils.createSmallChart(values.get(PersonalCabPresenter.Keys.GEN_RESEARCH.value),
                values.get(PersonalCabPresenter.Keys.USERS_RESEARCH.value),
                (ColumnChartView) findViewById(R.id.research_chart), getResources());
        ChartUtils.createSmallChart(values.get(PersonalCabPresenter.Keys.GEN_FUN.value),
                values.get(PersonalCabPresenter.Keys.USERS_FUN.value),
                (ColumnChartView) findViewById(R.id.fun_chart), getResources());
    }

    @Override
    public void renderBalance(String balance) {
        ((TextView) findViewById(R.id.user_balance_amount)).setText(balance);
    }

    @Override
    public void renderAmountOfFormsInProcess(String formsInProcess) {
        ((TextView) findViewById(R.id.forms_in_process_amount)).setText(formsInProcess);
    }

    @Override
    public void renderTimeOfLastTransaction(String time) {
        ((TextView) findViewById(R.id.last_transaction_time_amount)).setText(time);
    }

    @Override
    public void renderAmountOfTransactions(String amountOfTransactions) {
        ((TextView) findViewById(R.id.transactions_amount_amount)).setText(amountOfTransactions);
    }

    @Override
    public void renderAmountOfCharityTransactions(String amountOfCharityTransactions) {
        ((TextView) findViewById(R.id.charity_transactions_amount)).setText(amountOfCharityTransactions);
    }

    @Override
    public void renderListOfPassedForms(List<Form> passedForms) {
        RecyclerView usersPassedForms = (RecyclerView) findViewById(R.id.user_form_recycler);
        UserFormItemAdapter userFormItemAdapter = new UserFormItemAdapter(passedForms, personalCabPresenter);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        usersPassedForms.setLayoutManager(horizontalLayoutManager);
        usersPassedForms.setAdapter(userFormItemAdapter);
    }

    @Override
    public void noRenderFormsBlock() {
        RelativeLayout formsLayout = (RelativeLayout) findViewById(R.id.forms_block);
        formsLayout.setVisibility(View.GONE);
    }

    @Override
    public void renderListOfEmails(List<UserEmailDto> emails) {
        RecyclerView usersEmails = (RecyclerView) findViewById(R.id.user_email_recycler);
        UserEmailAdapter userEmailAdapter = new UserEmailAdapter(emails);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        usersEmails.setLayoutManager(verticalLayoutManager);
        usersEmails.setAdapter(userEmailAdapter);
    }

    @Override
    public void renderListOfPhones(List<UserPhoneDto> phones) {
        RecyclerView usersPhones = (RecyclerView) findViewById(R.id.user_phone_recycler);
        UserPhoneAdapter userPhoneAdapter = new UserPhoneAdapter(phones);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        usersPhones.setLayoutManager(verticalLayoutManager);
        usersPhones.setAdapter(userPhoneAdapter);
    }

    @Override
    public void showPerformTransactionDialog() {
        cashoutDialog = new CashoutDialog(personalCabPresenter, getContext());
    }

    @Override
    public void showTransactionHistoryDialog(List<TransactionEventsContainerDto> eventsWithTransactions) {
        transactionDialog = new TransactionDialog(getContext(), eventsWithTransactions);
    }

    @Override
    public void setCanApprove(boolean canApprove) {
        cashoutDialog.setCanApprove(canApprove);
    }

    @Override
    public void setCanApprovePhone(boolean canApprove) {
        phoneConfirmationDialog.canApprovePhone(canApprove);
    }

    @Override
    public void createConfirmationCharityPopup(String charityTarget) {
        confirmationDialog = new ConfirmationDialog(getContext());
        confirmationDialog.setCharityConfirmationText(charityTarget, getContext());
    }

    @Override
    public void createConfirmationPhonePopup(String phone) {
        confirmationDialog = new ConfirmationDialog(getContext());
        confirmationDialog.setPhoneConfirmationText(phone, getContext());
    }

    @Override
    public void createPhoneConfirmationDialog(Float money) {
        phoneConfirmationDialog = new PhoneConfirmationDialog(personalCabPresenter, getContext(), money);
    }

    @Override
    public void createAllTransactionDialog() {
        findViewById(R.id.show_all_transactions_button).setOnClickListener(view ->
                new MaterialDialog.Builder(getContext())
                        .customView(R.layout.transactions_popup, true)
                        .build()
                        .show()
        );
    }

    @Override
    public void showMessageWithUserBalance(Float balance) {
        cashoutDialog.setMessageWithUserBalance(balance, getContext());
    }

    @Override
    public void showMessageWithUserPhone(String phone, Float moneyUAN) {
        cellConfirmationDialog.showMessageWithUserPhone(phone, cashoutDialog.enteredMoney, moneyUAN, getContext());
    }

    @Override
    public void createCellConfirmationDialog() {
        cellConfirmationDialog = new CellConfirmationDialog(personalCabPresenter, getContext());
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultPersonalCabView defaultProfileView);
    }
}