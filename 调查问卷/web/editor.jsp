<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-hans">
<head>
    <meta charset="UTF-8">
    <title>调查问卷 | 编辑页面 | 无样式版</title>
    <style>
        .active::before {
            content: '* ';
        }
    </style>
</head>
<body>
    <h1>编辑页面</h1>
    <hr>
    <h2>添加问题区</h2>
    <div>
        <p id="add-radio">添加一个单选题</p>
        <p id="add-checkbox">添加一个多选题</p>
        <p id="add-text">添加一个陈述题</p>
    </div>
    <hr>
    <h2>Tab 显示区</h2>
    <div id="basic-tab" data-pid="0">基本信息 Tab</div>
    <div id="tabs"></div>
    <div id="thanks-tab" data-pid="-1">致谢信息 Tab</div>
    <div id="add-tab">添加一页</div>
    <hr>
    <h2>主题内容区</h2>
    <div id="content"></div>

    <script src="/js/ajax.js" charset="utf-8"></script>
    <script src="/js/survey.js" charset="utf-8"></script>
    <script src="/js/editor-ui.js" charset="utf-8"></script>
    <script src="/js/editor.js" charset="utf-8"></script>
    <script>
        window.onload = function () {
            Survey.load(<%= request.getParameter("sid") %>)
                .then(survey => {
                    let editorUI = new EditorUI();
                    let editor = new Editor(survey, editorUI);
                    editor.start();
                })
                .catch(error => {
                    console.error(error);
                });
        };
    </script>
</body>
</html>