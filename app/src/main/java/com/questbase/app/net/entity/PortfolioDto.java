package com.questbase.app.net.entity;

import com.questbase.app.utils.Objects;

public class PortfolioDto {

    public long id;
    public PersonalResult personalResult;
    public String slug;
    public String img;
    public String research;
    public String audience;
    public String description;
    public String expertise;

    public PortfolioDto(Builder builder) {
        id = builder.id;
        slug = builder.slug;
        img = builder.img;
        research = builder.research;
        audience = builder.audience;
        description = builder.description;
        expertise = builder.expertise;
        personalResult = builder.personalResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                slug,
                img,
                research,
                audience,
                description,
                expertise,
                personalResult);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PortfolioDto)) {
            return false;
        }
        PortfolioDto example = (PortfolioDto) object;
        return Objects.equal(id, example.id)
                && Objects.equal(slug, example.slug)
                && Objects.equal(img, example.img)
                && Objects.equal(research, example.research)
                && Objects.equal(audience, example.audience)
                && Objects.equal(description, example.description)
                && Objects.equal(personalResult, example.personalResult)
                && Objects.equal(expertise, example.expertise);
    }

    public static class Builder {
        private long id;
        private String slug;
        private String img;
        private String research;
        private String audience;
        private String description;
        private String expertise;
        private PersonalResult personalResult;

        public Builder(long value) {
            id = value;
        }

        public Builder slug(String val) {
            slug = val;
            return this;
        }

        public Builder img(String val) {
            img = val;
            return this;
        }

        public Builder research(String val) {
            research = val;
            return this;
        }

        public Builder audience(String val) {
            audience = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder expertise(String val) {
            expertise = val;
            return this;
        }

        public Builder personalResult(PersonalResult val) {
            personalResult = val;
            return this;
        }

        public PortfolioDto build() {
            return new PortfolioDto(this);
        }
    }
}