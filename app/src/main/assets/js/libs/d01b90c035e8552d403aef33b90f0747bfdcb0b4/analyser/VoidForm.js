define(function(require) {

    var VoidForm = function (model,data) {
        this.model = model;
        this.data = data;
    };

    VoidForm.prototype.analyse = function() {
        return {};
    };

    VoidForm.prototype.validate = function() {
        var errors=[];
        return errors;
    }

    return VoidForm;
});