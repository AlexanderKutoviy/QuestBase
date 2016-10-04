define(function(require) {

    var PersonalForm = function (model,data) {
        this.model = model;
        this.data = data;
    };

    PersonalForm.prototype.analyse = function() {
        var order = this.model.getMetadata().map(function(v){ return v.id});
        var ansMap = this.model.getAnswers();
        var ans = order.map(function(v){return ansMap[v]||null});


        var ansWeights = this.data.weights || 1;
        //if (isset($params["weight"])) {
        //    $ansWeights = $params["weight"];
        //} else {
        //    $ansWeights = 1;
        //}

        //if (isset($params["gender"])) {
        //    $gender = $params["gender"];
        //}


        var res = this.data.results;
        var $d = [];

        var len = res.length;

        for (k in res) {
            var answer=res[k];
            var points = answer.points;

            var p = ans.map(function (a, i) {
                var p = points[i];
                if (!Array.isArray(p) || p.length == 0)
                    return 0;
                if (a === null || a === "")
                    return 0;

                if (a instanceof Object && a["value"])
                    a = a.value;

                if (!(a instanceof Array))
                    a = a.split(",");
                var r = a.length == 0 ? 0 : a.reduce(function (s, b) {
                    return s + p[b]
                },0) / a.length;
                //console.log([r,p,a])
                if (ansWeights && Array.isArray(ansWeights))
                    r = r * ansWeights[i];
                return r;
            });

            res[k].dist = p.reduce(function (a, b) {
                return a + b
            });

            //console.log([ans, p, res[k]]);

            res[k].img = res[k].img || (k+"");
        }

        res.sort(function (a, b) {
            if (a.dist == b.dist)
                return 0;
            return (a.dist < b.dist) ? +1 : -1;
        });

        if (this.data.oneResult)
            res=[res[0]];

        return res;
    };

    /** CLI use only **/


    PersonalForm.prototype.validate = function() {
        var errors=[];
        var fs = require("fs");
        var that = this;

        var data = this.model.getMetadata();

        if (!this.data.results)
            errors.push("Для аналізатора не вказано параметр results");
        else {
            var ans = this.data.results
                .map(function (v, i) {
                    if (v.points.length!=data.length)
                        errors.push("Результат №" + i + " має в points " + v.points.length + " елементів, але в опитуванні " + data.length + " питаннь.");
                    else {
                        v.points.map(function (p, j) {
                            if (p.length > 0) {
                                if (data[j].items.length != p.length)
                                    errors.push("Результат №" + i + ", points[" + j + "] має довжину " + p.length + ", a очікується " + data[j].items.length);
                            }
                        });
                    }
                });

            // var imgs = this.data.results.map(function (v, i) {
            //     if (!fs.existsSync(that.model.getAssetDir()+"/fb/"+ v.img+".jpg"))
            //         errors.push("File fb/"+ v.img+".jpg not found, but mentioned at result #"+i);
            // });


        }

        return errors;
    };

    return PersonalForm;
});