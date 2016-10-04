define(function(require) {

    var TestForm = function (model,data) {
        this.model = model;
        this.data = data;
    };

    TestForm.prototype.analyse = function() {

        var right_ans = this.data.answers;
        var weights = this.data.weights;

        //$points = $this->CountRes($right_ans);
        var order = this.model.getMetadata().map(function(v){ return v.id});
        var ansMap = this.model.getAnswers();
        var ans = order.map(function(v){
            var a = ansMap[v];
            if (a instanceof Object && a["value"])
                a = a.value;
            return a || null
        });

        var ok = ans.map(function(a,i){
            var right = right_ans[i];
            var res;

            if (weights && weights[i]==0)
                return 0;

            if (right===null || right===undefined || right==="")
                res = 0;
            else if (Array.isArray(right))
                res = right.map(function(v){return v+"";}).indexOf(a+"") != -1;
            else
                res = right==a;

            res = res?1:0;

            if (weights)
                res = res*weights[i];

            return res;
        });

        var okCount = ok.reduce(function(s,a,i,A){ return s + a });

        //console.log(okCount)

        var res = [];

        var i, j;
        for (i in this.data.results) {
            var v = this.data.results[i];
            var cnt = Array.isArray(v.answerCount) ? v.answerCount : [v.answerCount];

            for (j in cnt)
            {
                if (cnt[j]==okCount)
                {
                    res=[{
                        dist: okCount,
                        title: v.title,
                        desc: v.desc,
                        img: v.img===undefined ? okCount : v.img
                    }];
                    break;
                }
            }
            if (res.length>0)
                break;
            //var sum = cnt.reduce(function(s,a){ return s + (a==okCount?1:0)});
            //console.log([cnt, okCount, sum]);
            //if (sum>0) {
            //
            //}
        }

        return res;
    };

    TestForm.prototype.validate = function() {
        var errors=[];

        var data = this.model.getMetadata();
        var qCount = data.length;
        var i,j,k;

        if (!this.data.results)
            errors.push("Для аналізатора не вказано параметр results");
        else
        {
            var ans = this.data.results
                .map(function(v,i){ return v.answerCount; });
            var ansMap={};
            for (k in ans)
            {
                var cnt = Array.isArray(ans[k]) ? ans[k] : [ans[k]];
                for (i in cnt)
                {
                    if (ansMap[cnt[i]]!==undefined)
                        errors.push("answerCount "+cnt[i]+" зустрічається в результаті №"+k+" та в результаті №"+ansMap[cnt[i]]);
                    ansMap[cnt[i]] = k;
                }
            }

            for (i=0;i<qCount;++i)
            {
                if (ansMap[i]===undefined)
                    errors.push("answerCount="+i+" не зустрічається серед жодного результату");
            }
        }

        if (!this.data.answers)
            errors.push("Для аналізатора не вказано параметр answers");
        else
        {
            if (this.data.weights) {
                if (this.data.weights.length != data.length)
                    errors.push("Кількість елементів в масиві answers відрізняється від кількості питаннь. Питаннь " + data.length + " штук, а answers.length=" + this.data.answers.length);

                if (this.data.weights.length != this.data.answers.length)
                    errors.push("Якщо weights вказано, та їх має бути стільки ж як і answers. weights.length=" + weights.length + " answers.length=" + this.data.answers.length);
            }

            for(k in this.data.answers)
            {
                var ans = Array.isArray(this.data.answers[k]) ? this.data.answers[k] : [this.data.answers[k]];
                var possible = data[k].items;
                if (possible!==undefined) {
                    for (i in ans) {
                        if (this.data.weights && this.data.weights[k] == 0)
                            continue;

                        if (possible[ans[i]] === undefined)
                            errors.push("answers[" + k + "][" + i + "]=" + ans[i] + " не знайдено серед доступних варіантів відповідей");
                    }
                }
                else {
                    if (this.data.answers[k] !== null && this.data.answers[k] !== undefined && ans.length > 0)
                        errors.push("answers[" + k + "] очікується пустим (null, undefined, []) так як для питання #" + k + " не встановлено items (type=" + data[k].type + ")");
                }
            }
        }

        return errors;
    }

    return TestForm;
});