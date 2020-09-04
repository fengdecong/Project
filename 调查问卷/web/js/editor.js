"use strict";

// 处理逻辑部分
class Editor {
    survey;
    editorUI;

    constructor(survey, editorUI) {
        this.survey = survey;
        this.editorUI = editorUI;
    }

    bindEvents() {
        this.editorUI.getAddTabButton().onclick = this.returnAddTabAction();
        this.editorUI.getBasicTabButton().onclick = this.returnActiveAction();
        this.editorUI.getThanksTabButton().onclick = this.returnActiveAction();
        this.editorUI.getAddRadioButton().onclick = this.returnAddRadioAction();
    }

    returnAddRadioAction() {
        let editor = this;

        return function (evt) {
            if (editor.editorUI.currentPid <= 0) {
                // pid == 0: 基本信息
                // pid == -1: 致谢词
                return;
            }

            editor.editorUI.addRadio(function (question) {
                editor.survey.addQuestion(editor.editorUI.currentPid, question);
                editor.survey.save();
                editor.activePage(editor.editorUI.currentPid);
            });
        };
    }

    returnActiveAction() {
        let editor = this;

        return function (evt) {
            // this 代表的就是点击了哪个 html 元素
            // html 标准上规定了，当属性是以 data- 开头，就可以直接通过 dataset 获取
            // 默认获取是 string 类型，我们通过 parseInt，转化为 int 类型
            let pid = parseInt(this.dataset.pid);   // 获取元素上的 data-pid=？

            editor.activePage(pid);
        };
    }

    returnRemoveAction() {
        let editor = this;

        return function (pid, isCurrentActive) {
            // 首先删除 model 中的数据
            editor.survey.removePage(pid);
            // 提交到服务端进行保存
            editor.survey.save();

            if (isCurrentActive) {
                // 重新激活，前面的一个 tab
                editor.activePage(pid - 1);
            }
        };
    }

    returnAddTabAction() {
        let editor = this;      // 这里的 this 还是 Editor 对象

        // 直接返回一个方法
        return function (evt) {
            // 向 Survey 对象中新增一页，并得到 pid
            // 这里不能用 this.survey 只能用 editor.survey，因为这里的 this 不是
            // Editor 对象，而是 html 元素对象
            let pid = editor.survey.addPage();

            // 新增一个 tab 页（只要和 html 有关系，全部放到 Editor-UI)
            editor.editorUI.addTab(pid, editor.returnActiveAction(), editor.returnRemoveAction());

            // 激活该 tab 页
            editor.activePage(pid);
        };
    }

    activePage(pid) {
        // 把原来处于激活状态的页面设置为非激活状态（tab 显示、content 内容）
        this.editorUI.inactive(pid);

        let data;
        let saveAction;
        let editor = this;
        if (pid === 0) {
            data = this.survey.getBasic();
            saveAction = function (title, brief, coverUrl) {
                editor.survey.setTitle(title);
                editor.survey.setBrief(brief);
                editor.survey.setCoverUrl(coverUrl);
                editor.survey.save();
            };
        } else if (pid === -1) {
            data = this.survey.getThanks();
            saveAction = function (thanks) {
                editor.survey.setThanks(thanks);
                editor.survey.save();
            }
        } else {
            data = this.survey.getPage(pid);
        }
        this.editorUI.active(pid, data, saveAction);
    }

    start() {
        this.bindEvents();

        for (let i = 0; i < this.survey.getPageLength(); i++) {
            this.editorUI.addTab(i + 1, this.returnActiveAction(), this.returnRemoveAction());
        }

        this.activePage(0);
    }
}