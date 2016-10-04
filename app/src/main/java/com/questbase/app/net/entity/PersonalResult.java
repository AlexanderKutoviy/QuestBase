package com.questbase.app.net.entity;

import com.questbase.app.dao.personalresult.weeklyusercountcontainer.WeeklyUserCountContainer;
import com.questbase.app.utils.Objects;

import java.util.List;

public class PersonalResult {

    public long id;
    public List<PortfolioDto> projects;
    public WeeklyUserCountContainer sessions;

    public PersonalResult(long id, List<PortfolioDto> projects, WeeklyUserCountContainer sessions) {
        this.id = id;
        this.projects = projects;
        this.sessions = sessions;
    }

    public PersonalResult(List<PortfolioDto> projects, WeeklyUserCountContainer sessions) {
        this.projects = projects;
        this.sessions = sessions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projects, sessions);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PersonalResult)) {
            return false;
        }
        PersonalResult personalResult = (PersonalResult) object;
        return Objects.equal(id, personalResult.id)
                && Objects.equal(projects, personalResult.projects)
                && Objects.equal(sessions, personalResult.sessions);
    }
}