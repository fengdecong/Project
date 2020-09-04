"use strict";

/*
pid: 0         基本信息页
pid > 0:       普通的调查问卷页
pid: -1        致谢页
 */
class EditorUI {
    currentPid = 0;
    tabs = document.querySelector("#tabs");
    contentPanel = document.querySelector("#content");

    getAddTabButton() {
        // 返回 html  中的 id=add-tab 的元素
        return document.querySelector("#add-tab");
    }

    addTab(pid, activeAction, removeAction) {
        // <div>第 <pid> 页 <span>删除</span></div>
        let span = document.createElement("span");
        span.innerText = "删除";
        let editorUI = this;
        span.onclick = function (evt) {
            let tab = this.parentNode;
            let pid = parseInt(tab.dataset.pid);
            if (editorUI.currentPid > pid) {
                editorUI.currentPid -= 1;
            }

            // 把后续的 tab 中的 pid 全部减一
            let next = tab.nextSibling;
            while (next !== null) {
                let nextPid = parseInt(next.dataset.pid);
                console.log(nextPid);
                let newPid = nextPid - 1;

                next.firstChild.data = "第" + newPid + "页";
                next.setAttribute("data-pid", newPid);

                next = next.nextSibling;
            }

            // editorUI.currentPid 记录了当前哪个 pid 处在激活状态
            // pid 是目前要删除的 tab 的 pid
            // 所以，pid === editorUI.currentPid 意思是，判断
            // 当前要删除的是不是处在激活状态的 tab
            removeAction(pid, pid === editorUI.currentPid);

            editorUI.tabs.removeChild(tab);  // 把 tab 删除

            evt.stopPropagation();
        };

        let div = document.createElement("div");
        let text = document.createTextNode("第" + pid + "页");
        div.appendChild(text);
        div.appendChild(span);

        div.setAttribute("data-pid", pid);
        div.onclick = activeAction;     // 点击 tab 后，会激活该 tab

        this.tabs.appendChild(div);
    }

    inactive() {
        // 需要先确定之前的激活状态的 tab
        // 如何标识激活页 tab，通过添加 class="active"
        let currentActive = document.querySelector(".active");
        if (currentActive === null) {
            return;
        }

        currentActive.classList.remove("active");
        this.contentPanel.innerHTML = "";   // 视为把该元素的所有孩子全部删掉
    }

    active(pid, data, saveAction) {
        this.currentPid = pid;

        let toActiveTab = document.querySelector("[data-pid='" + pid + "']");
        toActiveTab.classList.add("active");

        if (pid === 0) {
            this.showBasicContent(data, saveAction);
        } else if (pid === -1) {
            this.showThanksContent(data, saveAction);
        } else {
            this.showPageContent(pid, data);
        }
    }

    showBasicContent(data, saveAction) {
        let pTitle = document.createElement("p");
        pTitle.innerText = data.title;
        pTitle.contentEditable = "true";

        let pBrief = document.createElement("p");
        pBrief.innerText = data.brief;
        pBrief.contentEditable = "true";

        let pCoverUrl = document.createElement("p");
        pCoverUrl.innerText = data.coverUrl;
        pCoverUrl.contentEditable = "true";

        let saveButton = document.createElement("div");
        saveButton.innerText = "保存";
        saveButton.onclick = function (evt) {
            let title = pTitle.textContent;
            let brief = pBrief.textContent;
            let coverUrl = pCoverUrl.textContent;

            saveAction(title, brief, coverUrl);
        };

        this.contentPanel.appendChild(pTitle);
        this.contentPanel.appendChild(pBrief);
        this.contentPanel.appendChild(pCoverUrl);
        this.contentPanel.appendChild(saveButton);
    }

    showThanksContent(data, saveAction) {
        let pThanks = document.createElement("p");
        pThanks.innerText = data.thanks;
        pThanks.contentEditable = "true";

        let saveButton = document.createElement("div");
        saveButton.innerText = "保存";
        saveButton.onclick = function (evt) {
            let thanks = pThanks.textContent;

            saveAction(thanks);
        };

        this.contentPanel.appendChild(pThanks);
        this.contentPanel.appendChild(saveButton);
    }

    showPageContent(pid, data) {
        console.log(data);
        for (let i = 0; i < data.questions.length; i++) {
            let question = data.questions[i];
            if (question.type === "单选题") {
                this.showRadioQuestionContent(question);
            } else if (question.type === "多选题") {
                this.showCheckboxQuestionContent(question);
            } else {
                this.showTextQuestionContent(question);
            }
        }
    }

    /*
    <p>【单选题】问题</p>
    <p>选项:</p>
    <ol>
        <li><input type="radio" disabled> 选项1</li>
        <li><input type="radio" disabled> 选项2</li>
    </ol>
     */
    showRadioQuestionContent(question) {
        let p = document.createElement("p");
        p.innerText = "【单选题】" + question.question;

        let label = document.createElement("p");
        label.innerText = "选项：";

        let ol = document.createElement("ol");
        for (let i = 0; i < question.options.length; i++) {
            let li = document.createElement("li");
            li.innerHTML = "<input type='radio' disabled> " + question.options[i];
            ol.appendChild(li);
        }

        this.contentPanel.appendChild(p);
        this.contentPanel.appendChild(label);
        this.contentPanel.appendChild(ol);
    }

    showCheckboxQuestionContent(question) {

    }

    showTextQuestionContent(question) {

    }


    getBasicTabButton() {
        return document.querySelector("#basic-tab");
    }

    getThanksTabButton() {
        return document.querySelector("#thanks-tab");
    }

    getAddRadioButton() {
        return document.querySelector("#add-radio");
    }

    /*
    <p contentEditable="true">请输入您的调查问题</p>
    <div>
        <p>选项</p>
        <p>选项</p>
    </div>
    <p>添加新选项</p>
    <p>保存</p>
     */
    addRadio(saveAction) {
        let pQuestion = document.createElement("p");
        pQuestion.innerText = "请输入您的调查问题";
        pQuestion.contentEditable = "true";

        let divOptions = document.createElement("div");
        for (let i = 0; i < 2; i++) {
            let pOption = document.createElement("p");
            pOption.innerText = "选项";
            pOption.contentEditable = "true";
            divOptions.appendChild(pOption);
        }

        let addNewOption = document.createElement("p");
        addNewOption.innerText = "添加新选项";
        addNewOption.onclick = function() {
            let pOption = document.createElement("p");
            pOption.innerText = "选项";
            pOption.contentEditable = "true";
            divOptions.appendChild(pOption);
        }

        let saveButton = document.createElement("p");
        saveButton.innerText = "保存";
        saveButton.onclick = function () {
            let question = {
                question: pQuestion.textContent,
                type: "单选题",
                options: []
            };

            let children = divOptions.childNodes;
            for (let i = 0; i < children.length; i++) {
                let pOption = children[i];
                let option = pOption.textContent;
                question.options.push(option);
            }

            console.log(question);

            saveAction(question);
        };

        this.contentPanel.appendChild(pQuestion);
        this.contentPanel.appendChild(divOptions);
        this.contentPanel.appendChild(addNewOption);
        this.contentPanel.appendChild(saveButton);
    }
}