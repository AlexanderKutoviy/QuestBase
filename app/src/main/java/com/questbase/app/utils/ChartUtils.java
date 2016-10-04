package com.questbase.app.utils;

import android.content.res.Resources;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.questbase.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class ChartUtils {

    public static void createBigChart(List<String> values,
                                      ColumnChartView columnChartView,
                                      Resources resources) {
        //TODO: RESPO-513 discuss axis signs
        Axis axisX = Axis.generateAxisFromCollection(Arrays.asList(0.5f, 5.5f, 10.5f, 15.5f, 20.5f, 25.5f),
                Arrays.asList("січ", "лют", "бер", "кві", "тра", "чер"));
        Axis axisY = Axis.generateAxisFromCollection(Stream.of(createValuesForAxisY(values))
                        .map(Float::parseFloat).collect(Collectors.toList()),
                createValuesForAxisY(values));
        columnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        columnChartView.setInteractive(false);
        List<Column> columns = new ArrayList<>();
        Stream.range(0, values.size()).forEach(i -> {
            SubcolumnValue subcolumnValue = new SubcolumnValue(Float.parseFloat(values.get(i)));
            subcolumnValue.setColor(resources.getColor(R.color.respondents_chart_color));
            Column column = new Column(Collections.singletonList(subcolumnValue));
            columns.add(column);
        });
        ColumnChartData columnChartData = new ColumnChartData(columns);
        columnChartData.setAxisXBottom(axisX);
        columnChartData.setAxisYLeft(axisY);
        columnChartView.setColumnChartData(columnChartData);
    }

    public static void createSmallChart(Float mid, Float self,
                                        ColumnChartView columnChartView,
                                        Resources resources) {
        columnChartView.setInteractive(false);
        Axis axis = Axis.generateAxisFromCollection(Arrays.asList(0f, 1f),
                Arrays.asList(String.valueOf(mid), String.valueOf(self)));
        List<Column> columns = Arrays.asList(new Column(Collections.singletonList(new SubcolumnValue(mid)
                        .setColor(resources.getColor(R.color.small_chart_grey_color)))),
                new Column(Collections.singletonList(new SubcolumnValue(self)
                        .setColor(resources.getColor(R.color.small_chart_red_color)))));
        ColumnChartData columnChartData = new ColumnChartData(columns);
        columnChartData.setAxisXBottom(axis);
        columnChartView.setColumnChartData(columnChartData);
    }

    private static List<String> createValuesForAxisY(List<String> values) {
        List<Float> floatValues = Stream.of(values).map(Float::parseFloat).collect(Collectors.toList());
        List<Float> axisValues = Arrays.asList(floatValues.get(0),
                floatValues.get(floatValues.size() / 2),
                floatValues.get(floatValues.size() - 1));
        return Stream.of(axisValues).map(String::valueOf).collect(Collectors.toList());
    }
}