package com.questbase.app.form.checkimage;

class CheckImageItem {
    private String imageName;
    private String id;

    public String getId() {
        return id;
    }

    CheckImageItem(String imageName, String id) {
        this.imageName = imageName;
        this.id = id;
    }

    String getImageName() {
        return imageName;
    }
}

