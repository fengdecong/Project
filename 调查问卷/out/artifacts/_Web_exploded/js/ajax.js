"use strict";   // 表示 js 要遵循最新的 ES6 语法

function apiGet(url) {
    // 使用了一个 Promise 类：Promise 的优点是可以让代码不要陷入回调地狱中。
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open("get", url);
        xhr.onload = function (ev) {
            if (xhr.status !== 200) {
                reject(xhr.responseText);   // 执行 catch 部分
                return;
            }

            // { status: 200, data: ... }
            let result = JSON.parse(xhr.responseText);
            if (result.status !== 200) {    // 首先得是 200，并且的是 int 类型
                reject(result.reason);  // 执行 catch 部分
            } else {
                resolve(result.data);   // 执行 then 部分
            }
        };
        xhr.onerror = function (ev) {
            reject(xhr.responseText);   // 执行 catch 部分
        };
        xhr.send();
    });
}

function apiPost(url, data) {
    // 使用了一个 Promise 类：Promise 的优点是可以让代码不要陷入回调地狱中。
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open("post", url);
        // POST 请求体的格式是 JSON 格式
        xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        xhr.onload = function (ev) {
            if (xhr.status !== 200) {
                reject(xhr.responseText);   // 执行 catch 部分
                return;
            }

            // { status: 200, data: ... }
            let result = JSON.parse(xhr.responseText);
            if (result.status !== 200) {    // 首先得是 200，并且的是 int 类型
                reject(result.reason);  // 执行 catch 部分
            } else {
                resolve(result.data);   // 执行 then 部分
            }
        };
        xhr.onerror = function (ev) {
            reject(xhr.responseText);   // 执行 catch 部分
        };
        xhr.send(JSON.stringify(data));
    });
}