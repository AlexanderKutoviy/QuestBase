function extend() {
    for (var i = 1; i < arguments.length; i++)
        for (var key in arguments[i])
            if (arguments[i].hasOwnProperty(key))
                arguments[0][key] = arguments[i][key];
    return arguments[0];
}
define(function (require) {

    var GenericFormModel = function (d) {
        this.data = d;
        this.questionById = {};

        for (k in this.data.questions) {
            var v = this.data.questions[k];
            this.questionById[v.id] = v;
        }

        this.setState({});
    };

    GenericFormModel.prototype.evalCondition = function (expression) {
        var model = this;
        var eq = function (a, b) {
            return a === null || b === null || a === undefined || b === undefined
                ? null
                : a == b;
        }
        var ans = function (qid, key) {
            var a = model.getAnswer(qid);
            if (a instanceof Object && !(a instanceof Array))
                a=a[key || "value"];
            return a;
        }
        var isset = function (qid) {
            var v = model.getAnswer(qid);
            return v!==null && v!==undefined;
        }
        var not = function (a) {
            return a === null || a === undefined
                ? null
                : !a;
        }
        var or = function () {
            var k;
            var res = false;
            for (k in arguments) {
                if (res === false && (arguments[k] === null || arguments[k] === undefined))
                    res = null;
                else if (arguments[k] === true)
                    return true;
            }

            return res;
        }
        var and = function () {
            var k;
            var res = true;
            for (k in arguments) {
                if (res === true && (arguments[k] === null || arguments[k] === undefined))
                    res = null;
                else if (arguments[k] === false)
                    return false;
            }

            return res;
        }
        var has = function (A, item) {
            var k;

            if (A === null || A === undefined || item === null || item === undefined)
                return null;
            if (!(item instanceof Array))
                item = [item];

            for (k in item) {
                if (item[k] === null || item[k] === undefined)
                    return null;

                if (A.map(function (v) {
                        return v + "";
                    }).indexOf(item[k] + "") == -1)
                    return A.indexOf(null) == -1 && A.indexOf(undefined) == -1 ? false : null;
            }
            return true;
        }
        var intersects = function (A, B) {
            var k;

            if (A === null || A === undefined || B === null || B === undefined)
                return null;

            if (A.length == 0 || B.length == 0)
                return false;

            var hasNull = false;
            for (k in A)
                if (A[k] === null || A[k] === undefined)
                    hasNull = true;

            for (k in B) {
                if (B[k] === null || B[k] === undefined)
                    hasNull = true;

                if (A.map(function (v) {
                        return v + "";
                    }).indexOf(B[k] + "") !== -1)
                    return true;
            }
            return hasNull ? null : false;
        }
        var randRange = function (id, from, till) {
            var k;

            for (k in arguments)
                if (arguments[k] === null || arguments[k] === undefined)
                    return null;

            if (!model.state.rand)
                model.state.rand = {};
            if (model.state.rand[id] === undefined) {
                model.state.rand[id] = Math.floor(Math.random() * (till - from + 1)) + from;
            }

            return model.state.rand[id];
        }

        try {
            return eval(expression);
        }
        catch (e) {
            return null;
        }
    };

    GenericFormModel.prototype.setState = function (st) {
        this.state = extend({
            answers: {},
            pastQuestions: []
        }, st);
        if (this.state.answers instanceof Array)
            this.state.answers = {};
    };

    GenericFormModel.prototype.getState = function () {
        return this.state;
    };
    GenericFormModel.prototype.getAnswers = function () {
        return this.state.answers;
    };

    GenericFormModel.prototype.getParams = function () {
        var res = {};
        for (var k in this.data)
            if (k != "questions" && k != "analyser")
                res[k] = this.data[k];

        //DO NOT RELY ON THIS. CAN BE REMOVED ANY TIME
        if (res["bg"])
            res["bg"] = res["bg"].replace(".jpg", "");

        return res;
    };

    GenericFormModel.prototype.getAnswer = function (qid) {
        return this.state.answers[qid + ""];
    };

    GenericFormModel.prototype.setAnswer = function (qid, answer) {
        this.state.answers[qid + ""] = answer;
    };

    GenericFormModel.prototype.validateAnswer = function (qid, answer) {
        var q = this.questionById[qid];
        var opts = q.items;
        if (opts instanceof Function)
            opts = opts(this);

        return answer in opts;
    };

    function normalizeItems(res) {
        res.items = res.items.map(function (v, i) {
            if (v instanceof Function)
                v = v.call(res, this);
            v = v instanceof Object ? v : {id: i, text: v};
            if (v.id === null || v.id === undefined)
                v.id = i;
            if (typeof v.id != 'string')
                v.id = v.id + "";
            return v;
        });
    }

    function normalizeBulkRadioParamTitles(res) {
        res.paramTitles = res.paramTitles.map(function(v, i) {
            if(typeof v === "string") {
                v = v ? { legend: v } : {}
            }

            v.id = v.id || i;

            v.text = v.text || (v.id + 1);

            // console.log(v);

            return v;
        });
    }

    GenericFormModel.prototype.getQuestion = function (qid, doNotShuffle) {
        //console.log(this.prevState);
        //
        //this.prevState = 1;
        qid = qid + "";

        var question = this.questionById[qid];
        var res = {};
        if (!question)
            return null;

        for (var k in question)
            if (question[k] instanceof Function && k!="shuffleItems" && k!="shuffle")
                res[k] = question[k].call(res, this);
            else
                res[k] = question[k];

        if (res.items)
            normalizeItems(res);

        if(res.type === "BulkRadio" || res.type === "Hierarchy")
            normalizeBulkRadioParamTitles(res);

        res.shuffle = res.shuffle || res.shuffleItems;
        var _ = require("lib/LoDash");
        res = _.cloneDeep(res);
        
        if (!doNotShuffle && res.shuffle) // defined and not false
        {
            var ShuffleHelper = require("lib/ShuffleHelper");
            if (!(res.shuffle instanceof Function))
                res.shuffle = ShuffleHelper.shuffle('items', res.shuffle);

            res.shuffle.call(this, this, res);
        }


        if (this.data.analyser.class == "lib/analyser/TestForm") {
            if (this.data.showRightAnswers) {
                //var qs = this.getMetadata();
                var qs = this.data.questions;
                for (i in qs) {
                    if (qs[i].id == res.id) {
                        res.rightAnswerId = this.data.analyser.answers[i];
                    }
                }
            }

        }

        return res;
    };

    GenericFormModel.prototype.getProgress = function () {
        // console.log(this.state.answers);
        //
        // console.log(this.state.pastQuestions);
        // console.log(this.state.futureQuestions);


        var pos = this.state.pastQuestions.length-1;

        var cntAfter = 0;

        if (pos>=0 && this.getAnswer(this.state.pastQuestions[pos])===undefined) {
            pos--;
            cntAfter = 1;
        }

        // this.data.questions - this.state.pastQuestions
        var futureQuestions = this.data.questions.filter(function(q) {
            return this.state.pastQuestions.indexOf(q.id) == -1;
        }, this);


        for (var i = 0; i < futureQuestions.length; i++) {
            var q = futureQuestions[i];
            if (this.evalCondition(q.cond) !== false) {
                // console.log('c');
                cntAfter++;
            }
        }

        //return Object.keys(this.state.answers).length / Object.keys(this.data.questions).length;
        return (pos + 1) / (pos + 1 + cntAfter);
        // return (pos + 1) / this.data.questions.length;
    };

    GenericFormModel.prototype.getMetadata = function () {
        var res = [];
        for (k in this.data.questions) {
            //var v = this.data.questions[k];
            var v = this.getQuestion(this.data.questions[k].id, true);
            var item = {
                id: v.id,
                text: v.text,
                type: v.type
            };

            if (v.type === "RadioGroup" || v.type === "CheckGroup" || v.type === "DropList"
                || v.type === "BulkRadio" || v.type === "BulkRadioImaged" || v.type === "RadioGroupImaged"
                || v.type === "CheckGroupImaged" || v.type === "CheckGroupMatrix" || v.type === "DropDownGroup"
                || v.type === "BestWorst")
                    item.items = v.items;

            if (v.type === "BulkRadio" || v.type === "BulkRadioImaged" || v.type === "DropDownGroup")
                item.paramTitles = v.paramTitles;

            if (v.allowCustom)
                item.allowCustom = v.allowCustom;

            if (v.img)
                item.img = v.img;
            res.push(item);
        }
        return res;
    };

    GenericFormModel.prototype.analyse = function () {
        if (!this.data.analyser)
            return [];

        var Analyser = require(this.data.analyser.class);
        var a = new Analyser(this, this.data.analyser);
        return a.analyse();
    };

    GenericFormModel.prototype.lastAnsweredQuestion = function () {
        var i;
        var N = this.data.questions.length;
        var lastQuestion = undefined;
        for (i = 0; i < N; ++i) {
            var q = this.data.questions[i];

            if (this.state.answers[q.id] !== undefined) {
                lastQuestion = q;
            }
        }
        // not doing 'return lastQuestion' here because of question augmentation logic in getQuestion
        return this.getQuestion(lastQuestion.id);
    };

    function pushNextQuestionToPast(model)
    {
        var pastL = model.state.pastQuestions.length;

        if (model.data.questions.length>pastL) {
            if(!model.data.nextQuestionSelector) {
                var nextQ = model.data.questions[pastL];
            } else {
                var nextQ = model.data.nextQuestionSelector(model, model.data.questions);
                if(nextQ === null || nextQ === undefined) {
                    return null;
                }
                if (model.state.pastQuestions.indexOf(nextQ.id)!==-1)
                    throw new Exception("Next question '"+nextQ+"' is already in past questions "+model.state.pastQuestions);
            }
            model.state.pastQuestions.push(nextQ.id);
            return nextQ;
        }

        return null;
    }

    GenericFormModel.prototype.nextQuestion = function () {
        // if(this.data.shuffle) {
        //     this.data.shuffle(this, extend(this.data, {id: "root"}));
        // }

        var res_q = null;

        if (!this.state.pastQuestions.length) {
            res_q = pushNextQuestionToPast(this);
            if(res_q === null) {
                return null;
            }
        } else {
            var pastq = this.state.pastQuestions;
            res_q = this.questionById[pastq[pastq.length-1]];
        }

        while (true)
        {
            if (this.state.answers[res_q.id] === undefined) {
                var c = true;
                if (res_q.cond) {
                    c = res_q.cond instanceof Function
                        ? res_q.cond(this)
                        : this.evalCondition(res_q.cond);
                }
                if (c === null)
                    throw new Error("Condition " + res_q.cond + " returns NULL for question " + res_q.id + ", when all previous questions are answered");
                if (c === true) {
                    break;
                }
            }

            res_q = pushNextQuestionToPast(this);

            if(res_q === null) {
                break;
            }
        }

        // for (i = 0; i < N; ++i) {
        //     var q = qs[i];
        //
        //     if (this.state.answers[q.id] !== undefined) {
        //         continue;
        //     }
        //
        //     if (q.cond) {
        //         var c = q.cond instanceof Function
        //             ? q.cond(this)
        //             : this.evalCondition(q.cond);
        //
        //         if (c === null)
        //             throw new Error("Condition " + q.cond + " returns NULL for question " + q.id + ", when all previous questions are answered");
        //         if (c === false)
        //             continue;
        //     }
        //
        //     _nextPos = i;
        //     break;
        // }

        return res_q==null?null:this.getQuestion(res_q.id);
    };

    /** CLI use only **/

    GenericFormModel.prototype.setAssets = function (dir, files) {
        this.assetDir = dir;
        this.assetFiles = files;
    };
    
    GenericFormModel.prototype.getAssetDir = function () {
        return this.assetDir;
    };
    GenericFormModel.prototype.validate = function () {
        var errors = [];
        var model = this;

        try {
            if (!this.data.analyser)
                errors.push("Відсутні налаштування аналізатора");
            else {
                var Analyser;
                try {
                    Analyser = require(this.data.analyser.class);
                } catch (e) {
                    errors.push("Не можу знайти аналізатор " + model.data.analyser.class + ": " + e.stack);
                }

                if (Analyser) {
                    var a = new Analyser(this, this.data.analyser);
                    errors = errors.concat(a.validate());
                }
            }
        }
        catch (e) {
            errors.push("Error: " + e.stack);
        }

        return errors;
    };

    return GenericFormModel;
});