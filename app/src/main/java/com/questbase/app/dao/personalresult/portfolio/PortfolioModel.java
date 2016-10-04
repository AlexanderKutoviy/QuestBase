package com.questbase.app.dao.personalresult.portfolio;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.net.entity.PortfolioDto;

@Table(database = RespoDatabase.class)
public class PortfolioModel extends RespoBaseModel {

    @Column
    @PrimaryKey
    public long id;

    @Column
    public String slug;

    @Column
    public String img;

    @Column
    public String research;

    @Column
    public String audience;

    @Column
    public String description;

    @Column
    public String expertise;

    public PortfolioModel() {
    }

    public PortfolioModel(PortfolioDto portfolioDto) {
        id = portfolioDto.id;
        slug = portfolioDto.slug;
        img = portfolioDto.img;
        research = portfolioDto.research;
        audience = portfolioDto.audience;
        description = portfolioDto.description;
        expertise = portfolioDto.expertise;
    }

    public PortfolioDto toProjectExample() {
        return new PortfolioDto.Builder(id)
                .slug(slug)
                .img(img)
                .research(research)
                .audience(audience)
                .description(description)
                .expertise(expertise)
                .build();
    }
}