(ns task-manager.views
  (:use [hiccup core page]
        [clojure.string :only (join)]))

 (defn master [fields & body]
   (html
    (doctype :html5)
    [:html (:html-attribs fields)
     [:head (:title fields)]
     [:body
      (include-js "js/angular.js")
      (include-js "js/app.js")
      body]]))

(defn task-div [{n :task/number desc :task/description}]
  [:div
   [:span n]
   [:span desc]])

;; without auxiliary libs for now
(defn task-json [{n :task/number desc :task/description}]
  (str "{\"number\":" n "," "\"description\":\"" desc "\"}"))

(defn index [tasks]
  (master {:title "Yeah" :html-attribs {:ng-app ""}}
          [:div {:id "ctrl" :ng-controller "TasksCtrl"}
           [:script (str
                     "window.onload = function() {"
                     "var el=document.getElementById('ctrl');"
                     "var scope=angular.element(el).scope();"
                     "scope.$apply(function() { scope.tasks =["
                     (join "," (map task-json tasks))
                     "];});}")]
           [:ul "Tasks" [:li {:ng-repeat "task in tasks"}
                         [:span "{{task.number}}"]
                         [:span "{{task.description}}"]]]
           [:input {:type "text" :ng-model "newDesc"}]
           [:button {:ng-click "createTask()"} "Create"]]))
