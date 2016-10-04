package com.questbase.app.personalresult;

import android.graphics.Bitmap;

import com.questbase.app.net.entity.PortfolioDto;

import java.util.List;

public interface PersonalResultView {

    void setupListeners();

    void renderAvatar(Bitmap img);

    void renderPopularityChart(float avg, float currentForm);

    void renderPassedFormsChart(float avg, float users);

    void renderRespondentsAmount(List<String> points);

    void renderProjectsExamples(List<PortfolioDto> portfolioDtos);
}