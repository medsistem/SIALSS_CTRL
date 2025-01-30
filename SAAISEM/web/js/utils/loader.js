/* global currentContext, idUsuario */

var statusLoader = false;

function toogleLoader(element, findParent) {
    findParent = findParent === undefined ? true : findParent;

    element = $(element);
    var parent = element.parents('div.row');
    if (!findParent) {
        parent = element;
    }

    if (statusLoader) {
        $("#loderGif").remove();
        if (findParent) {
            parent.show();
        }

        statusLoader = false;
        return;
    }

    var container = parent.parent();
    var html = '<div id="loderGif" class="row"> <div class="col-sm-12">' +
            ' <img style="width:30%" class="img-responsive center-block"' +
            'src="../imagenes/ajax-loader-1.gif" alt=""/> </div></div>';

    if (!findParent) {
        html = '<img id="loderGif" style="width:50%" class="img-responsive center-block"' +
                'src="../imagenes/ajax-loader-1.gif" alt=""/>';
    }

    container.append(html);
    if (findParent) {
        parent.hide();
    }

    statusLoader = true;

}