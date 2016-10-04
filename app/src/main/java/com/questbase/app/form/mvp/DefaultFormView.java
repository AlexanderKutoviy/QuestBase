package com.questbase.app.form.mvp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.gson.JsonObject;
import com.questbase.app.R;
import com.questbase.app.QuestBaseApplication;
import com.questbase.app.di.AppComponent;
import com.questbase.app.di.PerView;
import com.questbase.app.flowui.Router;
import com.questbase.app.flowui.RouterOwner;
import com.questbase.app.form.bulkimage.BulkRadioImageOptionsView;
import com.questbase.app.form.bulkradio.BulkRadioOptionsView;
import com.questbase.app.form.checkbox.CheckBoxOptionsView;
import com.questbase.app.form.checkimage.CheckImageView;
import com.questbase.app.form.openquestion.OpenQuestionView;
import com.questbase.app.form.radio.RadioButtonOptionsView;
import com.questbase.app.form.radioimage.RadioImageView;
import com.questbase.app.swipeview.SwiperView;

import javax.inject.Inject;

import flow.Flow;
import rx.functions.Action6;

public class DefaultFormView extends RelativeLayout
        implements FormView, SwiperView.OnNextViewListener, RouterOwner {

    private static final int ZERO_MARGIN_OR_PADDING = 0;
    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String ITEMS = "items";
    private static final String TEXT = "text";
    private static final String LEGEND_TITLES = "paramTitles";
    private static final String TAG = DefaultFormView.class.getSimpleName();
    private Typeface myriadProRegular;

    private OpenQuestionView openQuestionView;
    private SwiperView swipeContainer;
    private Router router;

    @Inject
    FormPresenter presenter;

    public DefaultFormView(Context context) {
        super(context);
    }

    public DefaultFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DaggerDefaultFormView_Component.builder()
                .appComponent(QuestBaseApplication.getAppComponent())
                .build().inject(this);
        setupSwiper(context);
    }

    @Override
    public void injectRouter(Router router) {
        this.router = router;
    }

    enum Type {

        INPUT_TEXT(DefaultFormView::setupInputTextQuestion, "InputText"),
        RADIO_GROUP(DefaultFormView::setupRadioGroupQuestion, "RadioGroup"),
        CHECK_GROUP(DefaultFormView::setupCheckGroupQuestion, "CheckGroup"),
        BULK_RADIO_IMAGED(DefaultFormView::setupBulkRadioImageQuestion, "BulkRadioImaged"),
        BULK_RADIO(DefaultFormView::setupBulkRadioQuestion, "BulkRadio"),
        RADIO_IMAGE(DefaultFormView::setupRadioImageQuestion, "RadioGroupImaged"),
        CHECK_IMAGE(DefaultFormView::setupCheckImageQuestion, "CheckGroupImaged"),
        RESULT(DefaultFormView::setupPreResult, "Result");

        private final Action6<DefaultFormView, ViewGroup.LayoutParams, ViewGroup, View, View, JsonObject> action;
        private final String jsonFieldName;

        Type(Action6<DefaultFormView, ViewGroup.LayoutParams, ViewGroup, View, View, JsonObject> action, String jsonFieldName) {
            this.action = action;
            this.jsonFieldName = jsonFieldName;
        }

        public void setupQuestion(DefaultFormView formView, ViewGroup.LayoutParams params,
                                  ViewGroup viewGroup, View view, View button, JsonObject json) {
            action.call(formView, params, viewGroup, view, button, json);
        }

        public static Type fromString(String str) {
            return Stream.of(values())
                    .filter(value -> value.jsonFieldName.equals(str))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Not found type for json field: " + str));
        }
    }

    public View makeViewFromJson(JsonObject question) {
        ViewGroup constructedView = new RelativeLayout(getContext());
        Type enumType = Type.fromString(question.get(TYPE).getAsString());
        if (!enumType.equals(Type.RESULT)) {
            constructedView.addView(setupQuestionTextView(question));
        }
        constructedView.setBackgroundColor(Color.TRANSPARENT);
        LayoutParams parametersForAnswers = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        parametersForAnswers.setMargins(
                ZERO_MARGIN_OR_PADDING,
                (int) getResources().getDimension(R.dimen.answers_margin_top),
                ZERO_MARGIN_OR_PADDING,
                (int) getResources().getDimension(R.dimen.answers_margin_bottom));

        TextView statisticText = setupStatisticTextView();
        Button nextButton = setupNextButton();
        constructedView.addView(statisticText);
        constructedView.addView(nextButton);
        enumType.setupQuestion(this, parametersForAnswers, constructedView, statisticText, nextButton, question);
        return constructedView;
    }

    @Override
    public void setCurrentViewInSwiper(View currentView) {
        swipeContainer.setCurrentContentView(currentView);
    }

    @Override
    public void setNextViewInSwiper(View nextView) {
        swipeContainer.setNextContentView(nextView);
    }

    @Override
    public void showTitle(String formTitle) {
        ((TextView) findViewById(R.id.form_title)).setText(formTitle);
    }

    @Override
    public void setMyriadProRegularFont(Typeface font) {
        myriadProRegular = font;
    }

    @Override
    public void onNextView() {
        presenter.moveToNextItem();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FormScreen screen = Flow.getKey(this);
        presenter.attachView(this, router, screen.getFormId(), screen.getFormVersion(), screen.getModel());
        presenter.onNextView();
        setupHeader();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
    }

    private void setupSwiper(Context context) {
        swipeContainer = (SwiperView) (LayoutInflater.from(context).inflate(
                R.layout.swiper_view, this, false));
        RelativeLayout.LayoutParams relativeLayoutParams = (RelativeLayout.LayoutParams) swipeContainer.getLayoutParams();
        relativeLayoutParams.addRule(RelativeLayout.BELOW, R.id.header);
        swipeContainer.setLayoutParams(relativeLayoutParams);
        addView(swipeContainer);
        swipeContainer.addListener(this);
    }

    private void setupHeader() {
        FormScreen screen = Flow.getKey(this);
        presenter.setupHeader(screen.getFormTitle());
    }

    private TextView setupQuestionTextView(JsonObject question) {
        TextView questionText = new TextView(getContext());
        questionText.setText(question.get(TEXT).getAsString());
        questionText.setTypeface(myriadProRegular);
        questionText.setTextSize(getResources().getDimension(R.dimen.form_quesion_text_size));
        int paddingQuestion = (int) getResources().getDimension(R.dimen.question_padding_side);
        questionText.setPadding(ZERO_MARGIN_OR_PADDING, paddingQuestion, paddingQuestion, paddingQuestion);

        LayoutParams lparamForText = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.question_text_size));
        lparamForText.setMargins((int) getResources().getDimension(R.dimen.question_margin_left),
                (int) getResources().getDimension(R.dimen.question_margin_top),
                (int) getResources().getDimension(R.dimen.question_margin_right),
                (int) getResources().getDimension(R.dimen.question_margin_bottom));
        questionText.setLayoutParams(lparamForText);
        questionText.setGravity(Gravity.CENTER_VERTICAL);
        questionText.setBackgroundColor(Color.TRANSPARENT);
        return questionText;
    }

    private TextView setupStatisticTextView() {
        TextView statisticText = new TextView(getContext());
        LayoutParams parametersForStatistic = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.question_text_size));
        parametersForStatistic.setMargins(
                (int) getResources().getDimension(R.dimen.answers_margin_left),
                (int) getResources().getDimension(R.dimen.statistic_margin_top),
                (int) getResources().getDimension(R.dimen.statistic_margin_right),
                (int) getResources().getDimension(R.dimen.statistic_margin_bottom));

        statisticText.setLayoutParams(parametersForStatistic);
        statisticText.setTypeface(myriadProRegular);
        statisticText.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        statisticText.setText(getResources().getString(R.string.statistic_link));
        statisticText.setTextSize(getResources().getDimension(R.dimen.form_statistic_text_size));
        statisticText.setTextColor(getResources().getColor(R.color.statistic_text_disable));
        statisticText.setEnabled(false);
        statisticText.setClickable(true);
        setListenerToStatisticLink(statisticText);
        statisticText.setVisibility(INVISIBLE);
        return statisticText;
    }

    private Button setupNextButton() {
        Button nextButton = new Button(getContext());
        LayoutParams parametersForNextButton = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.question_text_size));
        parametersForNextButton.setMargins(
                (int) getResources().getDimension(R.dimen.button_margin_left),
                (int) getResources().getDimension(R.dimen.button_margin_top),
                (int) getResources().getDimension(R.dimen.next_button_margin_right),
                (int) getResources().getDimension(R.dimen.button_margin_bottom));

        nextButton.setLayoutParams(parametersForNextButton);
        nextButton.setGravity(Gravity.CENTER);
        nextButton.setText(getResources().getString(R.string.next_button_text));
        setListenerToNextButton(nextButton);
        nextButton.setTextColor(getResources().getColor(R.color.white));
        nextButton.setBackgroundColor(getResources().getColor(R.color.tab_select));
        nextButton.setEnabled(false);
        nextButton.setClickable(true);
        return nextButton;
    }

    private void setupInputTextQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                        View statisticText, View button, JsonObject question) {
        openQuestionView = new OpenQuestionView(swipeContainer.getContext());
        openQuestionView.setLayoutParams(parametersForAnswers);
        statisticText.setVisibility(INVISIBLE);
        openQuestionView.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, openQuestionView.answered());
            swipeContainer.setCanSwipe(openQuestionView.answered());
        });
        makingView.addView(openQuestionView);
    }

    private void setupRadioGroupQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                         View statisticText, View button, JsonObject question) {
        RadioButtonOptionsView answersRadio = new RadioButtonOptionsView(
                swipeContainer.getContext(), question.getAsJsonArray(ITEMS),
                question.get(ID).getAsString());
        answersRadio.setLayoutParams(parametersForAnswers);
        answersRadio.setScrollContainer(true);
        answersRadio.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            swipeContainer.setCanSwipe(answersRadio.isItemCheck());
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, answersRadio.isItemCheck());
        });
        makingView.addView(answersRadio);
    }

    private void setupCheckGroupQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                         View statisticText, View button, JsonObject question) {
        CheckBoxOptionsView answersCheck = new CheckBoxOptionsView(swipeContainer.getContext(),
                question.getAsJsonArray(ITEMS), question.get(ID).getAsString());
        answersCheck.setLayoutParams(parametersForAnswers);
        answersCheck.setScrollContainer(true);
        answersCheck.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            swipeContainer.setCanSwipe(!answersCheck.isItemCheck());
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, !answersCheck.isItemCheck());
        });
        makingView.addView(answersCheck);
    }

    private void setupBulkRadioImageQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                             View statisticText, View button, JsonObject question) {
        FormScreen screen = Flow.getKey(this);
        BulkRadioImageOptionsView answersImage = new BulkRadioImageOptionsView(
                swipeContainer.getContext(), question.getAsJsonArray(ITEMS), screen.getFormId());
        answersImage.setLayoutParams(parametersForAnswers);
        answersImage.setScrollContainer(true);
        answersImage.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            swipeContainer.setCanSwipe(answersImage.isItemCheck());
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, answersImage.isItemCheck());
        });
        makingView.addView(answersImage);
    }

    private void setupBulkRadioQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                        View statisticText, View button, JsonObject question) {
        BulkRadioOptionsView answersBulkRadio = new BulkRadioOptionsView(
                swipeContainer.getContext(), question.getAsJsonArray(ITEMS),
                question.getAsJsonArray(LEGEND_TITLES));
        answersBulkRadio.setLayoutParams(parametersForAnswers);
        answersBulkRadio.setScrollContainer(true);
        makingView.addView(answersBulkRadio);
        answersBulkRadio.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            swipeContainer.setCanSwipe(answersBulkRadio.isItemCheck());
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, answersBulkRadio.isItemCheck());
        });
    }

    private void setupCheckImageQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                         View statisticText, View button, JsonObject question) {
        FormScreen screen = Flow.getKey(this);
        CheckImageView answersImage = new CheckImageView(
                swipeContainer.getContext(), question.getAsJsonArray(ITEMS), screen.getFormId());
        answersImage.setLayoutParams(parametersForAnswers);
        answersImage.setScrollContainer(true);
        answersImage.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            swipeContainer.setCanSwipe(!answersImage.isItemCheck());
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, !answersImage.isItemCheck());
        });
        makingView.addView(answersImage);
    }

    private void setupRadioImageQuestion(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                         View statisticText, View button, JsonObject question) {
        FormScreen screen = Flow.getKey(this);
        RadioImageView answersImage = new RadioImageView(
                swipeContainer.getContext(), question.getAsJsonArray(ITEMS), screen.getFormId());
        answersImage.setLayoutParams(parametersForAnswers);
        answersImage.setScrollContainer(true);
        answersImage.addResponseListener(response -> {
            presenter.onResponse(question.get(ID).getAsString(), response);
            swipeContainer.setCanSwipe(answersImage.isItemCheck());
            onStateStatisticNextButtonChange((TextView) statisticText, (Button) button, answersImage.isItemCheck());
        });
        makingView.addView(answersImage);
    }

    private void setupPreResult(ViewGroup.LayoutParams parametersForAnswers, ViewGroup makingView,
                                View statisticText, View button, JsonObject question) {

        RelativeLayout preResult = (RelativeLayout) (LayoutInflater.from(getContext()).inflate(
                R.layout.pre_result_page, this, false));
        preResult.findViewById(R.id.go_to_result).setOnClickListener(v -> presenter.onPersonalResultButtonClick());
        statisticText.setVisibility(INVISIBLE);
        makingView.removeView(button);
        makingView.addView(preResult);
    }

    private void setListenerToStatisticLink(TextView statisticText) {
        statisticText.setOnClickListener(v -> Log.d(TAG, "setListenerToStatisticLink:go to statistic"));
    }

    private void setListenerToNextButton(Button nextButton) {
        nextButton.setOnClickListener(v -> swipeContainer.moveToNextCard());
    }

    private void onStateStatisticNextButtonChange(TextView statisticText, Button nextButton, boolean isEnable) {
        if (isEnable) {
            statisticText.setTextColor(getResources().getColor(R.color.statistic_text_enable));
            nextButton.setBackgroundColor(getResources().getColor(R.color.pre_result_text));
            statisticText.setEnabled(true);
            nextButton.setEnabled(true);
        } else {
            statisticText.setTextColor(getResources().getColor(R.color.statistic_text_disable));
            nextButton.setBackgroundColor(getResources().getColor(R.color.tab_select));
            statisticText.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (openQuestionView != null) {
            openQuestionView.hideKeyboard();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @dagger.Component(dependencies = AppComponent.class)
    @PerView
    interface Component {
        void inject(DefaultFormView defaultFormView);
    }
}
