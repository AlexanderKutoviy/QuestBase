package com.questbase.app.form.radioimage;

class RadioImageItem {
    private String imageName;
    private Boolean checked = false;

    public RadioImageItem(String imageName) {
        this.imageName = imageName;
    }

    String getImageName() {
        return imageName;
    }

    public Boolean isCheck() {
        return checked;
    }

    public void setCheck(Boolean checked) {
        this.checked = checked;
    }
}

