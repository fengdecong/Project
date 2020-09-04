"use strict";

class Survey {
    // 在保存之前，检查 changed，如果确定有数据真正改变了
    // 才调用后端的接口
    // 因为，调用后台接口，需要付出很大成本：
    // 1. 网络请求
    // 2. 后台重新保存也一定有成本
    changed = false;

    // 属性，js 中没有访问限定，没有变量类型，所以只剩变量名
    // 这部分数据，运行起来的时候会被服务端的数据替换，我们只是暂时为了让 IDEA 更好的提示
    // 所以，先把结构列出来
    data = {
        sid: 1399,
        title: "...",
        brief: "...",
        coverUrl: "...",
        thanks: "...",
        pages: [
            {
                questions: [
                    {
                        question: "...",
                        type: "...",
                        options: []
                    }
                ]
            }
        ]
    };

    // 构造方法
    constructor(data) {
        this.data = data;
    }

    addPage() {
        // js 的数组类似 java 中的 list
        // 往数组中尾插一个数据
        this.data.pages.push({
           questions: []
        });

        console.log(this.data);
        return this.data.pages.length;
    }

    getPageLength() {
        return this.data.pages.length;
    }

    getBasic() {
        return {
            title: this.data.title,
            brief: this.data.brief,
            coverUrl: this.data.coverUrl
        };
    }

    getThanks() {
        return {
            thanks: this.data.thanks
        };
    }

    getPage(pid) {
        return this.data.pages[pid - 1];
    }

    removePage(pid) {
        // 删除下标冲 pid - 1 开始的，1 个元素
        if (this.data.pages[pid - 1].questions.length !== 0) {
            this.changed = true;
        }

        this.data.pages.splice(pid - 1, 1);
    }

    setTitle(title) {
        if (title !== this.data.title) {
            this.data.title = title;
            this.changed = true;
        }
    }
    setBrief(brief) {
        if (brief !== this.data.brief) {
            this.data.brief = brief;
            this.changed = true;
        }
    }
    setCoverUrl(coverUrl) {
        if (coverUrl !== this.data.coverUrl) {
            this.data.coverUrl = coverUrl;
            this.changed = true;
        }
    }
    setThanks(thanks) {
        if (thanks !== this.data.thanks) {
            this.data.thanks = thanks;
            this.changed = true;
        }
    }
    // 通过 ajax，把我们的数据提交给后台
    save() {
        console.log(this.data);
        if (this.changed) {
            this.changed = false;

            apiPost("/api/save-survey.json", this.data);
        }
    }

    addQuestion(pid, question) {
        this.data.pages[pid - 1].questions.push(question);
        this.changed = true;
    }

    // 根据 sid，从后台获取相应的 调查问卷 数据，并构建 Survey 对象
    static load(sid) {
        return apiGet("/api/get-survey.json?sid=" + sid)
            .then(data => {
                return new Survey(data);
            });
    }
}