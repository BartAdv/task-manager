(ns task-manager.views
  (:use [hiccup core page]
        [clojure.string :only (join replace-first)]))

(defn master [fields & body]
   (html
    (doctype :html5)
    [:html (:html-attribs fields)
     [:head (:title fields)]
     [:body
      (include-js "/js/angular.js")
      (include-js "/js/app.js")
      (include-css "/css/style.css")
      body]]))

(defn task-div [{n :number desc :description}]
  [:div
   [:span n]
   [:span desc]])

;; without auxiliary libs for now
(defn stringify [m]
  (str "{"
        (join "," (map 
                    (fn [[k v]] (str (str "\"" (replace-first (str k) ":" "") "\"") ": " (if (number? v) v (str "\"" v "\"")))) 
                    (seq m)))
       "}"))

(defn index [tasks]
  (do
    (println tasks)
    (master {:title "Yeah" :html-attribs {:ng-app ""}}
            [:div {:id "ctrl" :ng-controller "TasksCtrl"}
             [:script (str "ApplyToElementScope(document.getElementById('ctrl'), function(scope) { scope.tasks = ["
                            (join "," (map stringify tasks))
                            "];});")]
           [:ul "Tasks" [:li {:ng-repeat "task in tasks" :ng-click "selectTask(task['task/number'])"}
                        [:span "{{task['task/number']}}"]
                        [:span "{{task['task/description']}}"]
                        [:span "{{task['task/status']}}"]
                        [:div {:ng-show "isSelected(task)"}
                          [:input {:type "textarea" :ng-model "task['task/description']"}]
                          [:input {:type "text" :ng-model "task['task/status']"}]
                          [:button {:ng-click "update(task)"} "Update"]]]]
           [:input {:type "text" :ng-model "newTask.description"}]
           [:button {:ng-click "create()"} "Create"]])))

(defn details [task]
  (println task)
  (master {:title "Details" :html-attribs {:ng-app ""}}
          [:div {:id "ctrl" :ng-controller "TaskDetailsCtrl"}
           [:script (str "ApplyToElementScope(document.getElementById('ctrl'), function(scope) { scope.task ="
                          (stringify task)
                          ";});")]
           [:span "{{task['task/number']}}"]
           [:span "{{task['description']}}"]
           [:span "{{task['status']}}"]]))
