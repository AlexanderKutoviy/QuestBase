function(id, path, libPath){

    var req = requirejs.config({
        paths: {
            "lib": libPath
        },
        context:"context-"+libPath,
    });

    if (typeof models == 'undefined') models=[];

    if (!models[id]) {
        req(['form/' + id + '/form'], function(factory){
            models[id] = factory;
        });
    }
    return models[id];
}
