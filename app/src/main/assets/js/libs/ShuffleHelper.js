define(function (require) {
    // var jQuery = require("lib/JqueryExtend.js");

    // function deepCopy(instance) {
    //     if(instance instanceof Array) {
    //         var new_array = [];
    //         for(var i = 0; i < instance.length; i++) {
    //             new_array.push(deepCopy(instance[i]));
    //         }
    //         return new_array;
    //     }
    //     if(typeof instance === "object") {
    //         //console.log(instance);
    //         var new_object = {};
    //         for (var key in instance) {
    //             new_object[key] = deepCopy(instance[key]);
    //         }
    //        //console.log(new_object);
    //         return new_object;
    //     }
    //     return instance;
    // }

    function shuffleArray(array, random, idx) {
        idx = idx || Array.apply(null, Array(array.length)).map(function (_, i) {return i;});

        var i, a, b, t;
        for(i = 0; i < array.length * 4; i++) {
            a = Math.floor(random.double() * (idx.length));
            b = Math.floor(random.double() * (idx.length));
            t = array[idx[a]];
            array[idx[a]] = array[idx[b]];
            array[idx[b]] = t;
        }
    }

    function initRandom(options, model, question) {
        var seedId = options.seedId || "shuffle-"+question.id;

        model.state.rand = model.state.rand || {};
        model.state.rand[seedId] = model.state.rand[seedId] || (Math.random() + '');

        var Random = requirejs("lib/Random");
        return new Random(model.state.rand[seedId]);
    }

    function getIdx(items, options) {
        var i;
        var N = items.length;
        var idx = [];
        if (options === true) {
            for (i = 0; i < N; ++i)
                idx.push(i);
        } else {
            for (i = (options.exceptFirst ? options.exceptFirst : 0); i < N - (options.exceptLast ? options.exceptLast : 0); ++i)
                if (!options.except || options.except.indexOf(i) == -1)
                    idx.push(i);
        }
        return idx;
    }

    var ShuffleHelper={};

    ShuffleHelper.shuffle = function (shuffleWhat, options) {
        options = options || {};

        return function (model, data) {
            var random = initRandom(options, model, data);


            function _shuffle(_shuffleWhat, object) {
                _shuffleWhat = _shuffleWhat.slice(0);
                var nextIndex = _shuffleWhat.shift();
                if (_shuffleWhat.length == 0) {
                    var toShuffle = object[nextIndex];

                    if(!options.allowedPermutations) {
                        var idx = getIdx(toShuffle, options);
                        shuffleArray(toShuffle, random, idx);
                    } else {
                        var permutation = options.allowedPermutations[Math.floor(random.double()*options.allowedPermutations.length)];
                        var toShuffleCpy = toShuffle.slice(0);
                        for(var i = 0; i < permutation.length; i++) {
                            toShuffle[i] = toShuffleCpy[permutation[i]];
                        }
                    }
                }
                else {
                    for (var i = 0; i < object[nextIndex].length; i++) {
                        _shuffle(_shuffleWhat, object[nextIndex][i]);
                    }
                }
            }

            if (typeof shuffleWhat === "string") {
                shuffleWhat = [shuffleWhat];
            }

            _shuffle(shuffleWhat, data);
        };
    };

    // Fix for old versions
    ShuffleHelper.innerShuffle = function() {
        return ShuffleHelper.shuffle(['item', 'item']);
    }

    ShuffleHelper.composite = function(shuffles){
        return function(model, question) {
            for(var i = 0; i < shuffles.length; i++) {
                shuffles[i](model, question);
            }
        }
    };


    ShuffleHelper.selectorByGroups = function(options) {
        var _ = requirejs("lib/LoDash");

        function normalizeQid(model, id) {
            switch (typeof id) {
                case "number":
                    return model.data.questions[id].id;
                case "string":
                    return id;
            }
        }

        function getNextQidFromBlock(model, block) {
            if(block.innerShuffle) {
                if(block.innerShuffle === true) {
                    block.innerShuffle = ShuffleHelper.shuffle('items');
                }

                block.innerShuffle(model, block);
            }

            // console.log(block.items);

            // console.log(model.state.pastQuestions);

            var res = _.find(block.items, function(item) {
                return !_.includes(model.state.pastQuestions, item);
            });

            // console.log(res);

            return res;
        }

        return function (model, questions) {
            var opts = _.cloneDeep(options) || {};

            var blocksById = {};

            var origOrder = [];

            for (var i = 0; i < opts.blocks.length; i++) {
                opts.blocks[i].items = opts.blocks[i].items.map(function (qid) {
                    return normalizeQid(model, qid);
                });

                blocksById[opts.blocks[i].id] = opts.blocks[i];
            }



            var origBlockOrder = Object.keys(blocksById);
            var blockOrder = origBlockOrder.slice(0);

            if(opts.outerShuffles) {
                for (var i = 0; i < opts.outerShuffles.length; i++) {
                    var shuffle = opts.outerShuffles[i];

                    if (shuffle instanceof Array) {
                        shuffle = {
                            items: shuffle,
                            shuffle: ShuffleHelper.shuffle('items')
                        }
                    }

                    shuffle.id = "blockGroup" + i;

                    var orig_order = shuffle.items.slice(0);
                    // console.log('orig', orig_order);

                    model.state.outerShuffles = model.state.outerShuffles || [];
                    // console.log('outerShuffles:', model.state.outerShuffles);
                    if(!model.state.outerShuffles[i]) {
                        shuffle.shuffle(model, shuffle);
                        model.state.outerShuffles[i] = shuffle.items;
                    } else {
                        shuffle.items = model.state.outerShuffles[i];
                    }


                    // console.log('shuf', shuffle.items);

                    for (var j = 0; j < orig_order.length; j++) {
                        // console.log('preblock', blockOrder);
                        // console.log('zzz', blockOrder, orig_order[j], origBlockOrder.indexOf(orig_order[j]), shuffle.items[j]);

                        blockOrder[_.lastIndexOf(blockOrder, orig_order[j])] = shuffle.items[j];
                        // console.log('postblock', blockOrder);
                    }

                    model.state.blockOrder = blockOrder;
                }
            }

            // console.log(blockOrder);






            var pastQ = model.state.pastQuestions;

            var current_block = _.find(opts.blocks, function(block) {
                return _.includes(block.items, _.last(pastQ));
            });

            if(current_block) {
                var nextQid = getNextQidFromBlock(model, current_block);

                if(nextQid) {
                    return model.questionById[nextQid];
                }
            }

            // There is no current block or it has ended


            var order = [];
            for(var i = 0; i < model.data.questions.length; i++) {
                var qs = model.data.questions[i];

                var block = _.find(opts.blocks, function(block) {
                    return _.includes(block.items, qs.id);
                });

                if(block) {
                    if(_.isEmpty(order) || _.last(order).bid !== block.id) {
                        order.push({
                            type: "block",
                            bid: block.id
                        });
                    }
                } else {
                    order.push({
                        type: "question",
                        qid: qs.id
                    });
                }
            }

            _.forEach(order, function(item) {
                if(item.type === "block") {
                    item.bid = blockOrder[origBlockOrder.indexOf(item.bid)];
                }
            });

            // console.log(order);


            var lastOrderItemIndex =
                _.findIndex(order,
                    current_block
                    ? _.matchesProperty('bid', current_block.id)
                    : _.matchesProperty('qid', _.last(pastQ))
                );

            for(;;lastOrderItemIndex++) {
                var nextOrderItem = order[lastOrderItemIndex + 1];

                if(!nextOrderItem) {
                    return null;
                }

                switch(nextOrderItem.type) {
                    case "block":
                        var block = blocksById[nextOrderItem.bid];

                        // console.log('block', block);

                        if(!model.evalCondition(block.cond || true)) {
                            model.state.skippedBlocks = model.state.skippedBlocks || [];
                            model.state.skippedBlocks.push(block.id);
                            // console.log('skipped');
                            continue;
                        }

                        if(_.isEmpty(block.items)) {
                            continue;
                        }

                        // console.log('sk blocks', model.state.skippedBlocks);

                        return model.questionById[getNextQidFromBlock(model, block)];
                    case "question":
                        return model.questionById[nextOrderItem.qid];
                }
            }
        }
    }

    return ShuffleHelper;
});