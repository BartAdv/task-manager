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

(defn task-div [{n :number desc :description}]
  [:div
   [:span n]
   [:span desc]])

;; without auxiliary libs for now
;; this is only needed for initial fill, small experiment and
;; that's why it does not use tasks/task->json
(defn task->json [{n :number desc :description status :status}]
  (format "{ \"number\": %d,
             \"description\": \"%s\",
             \"status\": \"%s\"
          }"
          n
          desc
          status))

(defn index [tasks]
  (do
    (println tasks)
    (master {:title "Yeah" :html-attribs {:ng-app ""}}
            [:div {:id "ctrl" :ng-controller "TasksCtrl"}
             [:script (str
                     "window.onload = function() {"
                     "var el=document.getElementById('ctrl');"
                     "var scope=angular.element(el).scope();"
                     "scope.$apply(function() { scope.tasks =["
                     (join "," (map task->json tasks))
                     "];});}")]
           [:ul "Tasks" [:li {:ng-repeat "task in tasks" :ng-click "selectTask(task)"}
                         [:span "{{task.number}}"]
                         [:span "{{task.description}}"]
                         [:span "{{task.status}}"]]]
           [:input {:type "text" :ng-model "task.description"}]
           [:button {:ng-click "createTask()"} "Create"]
           [:input {:type "text" :ng-model "task.status"}]
           [:button {:ng-click "updateStatus()"} "Update status"]])))
