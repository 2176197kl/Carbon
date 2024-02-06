$(function () {
   // 点击表单 uploadForm 的点击按钮时，触发 upload 函数
   $("#uploadForm").submit(upload);
});
var fileInput = $("input[type='file']")[0];
function upload() {

    if (!fileInput.files || !fileInput.files[0]) {
        alert("请选择文件");
        return false;
    }

    var formData = new FormData();
    formData.append("file", fileInput.files[0]);

    $.ajax({
        url: CONTEXT_PATH + "/user/header/url",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (res) {
            // 刷新界面
            window.location.reload();
        }
    });

    return false;
}