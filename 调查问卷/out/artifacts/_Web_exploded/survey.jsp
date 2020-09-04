<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <main>
        <form method="post" action="/submit-survey">

        </form>
    </main>
    <script src="/js/ajax.js"></script>
    <script>
        apiGet("/api/get-survey.json?sid=" + <%= request.getParameter("sid") %>)
            .then(survey => {
               let main = document.querySelector("form");
               let h1 = document.createElement("h1");
               h1.innerText = survey.title;
               main.appendChild(h1);

               let h2 = document.createElement("h2");
               h2.innerText = survey.brief;
               main.appendChild(h2);

               main.appendChild(document.createElement("hr"));

               for (let i = 0; i < survey.pages.length; i++) {
                   for (let j = 0; j < survey.pages[i].questions.length; j++) {
                       let question = survey.pages[i].questions[j];

                       if (question.type === "单选题") {
                           let p = document.createElement("p");
                           p.innerText = question.question;
                           main.appendChild(p);

                           for (let x = 0; x < question.options.length; x++) {
                               let input = document.createElement("input");
                               input.name = question.question;
                               input.type = "radio";
                               input.value = question.options[x];
                               main.appendChild(input);

                               let p = document.createElement("p");
                               p.innerText = question.options[x];
                               main.appendChild(p);
                           }
                       }
                   }

                   main.appendChild(document.createElement("hr"));
               }

               let button = document.createElement("button");
               button.type = "submit";
               button.innerText = "提交";

               main.appendChild(button);
            });
    </script>
</body>
</html>