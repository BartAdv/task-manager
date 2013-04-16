(ns task-manager.views
  (:use [hiccup core page]))

(defn master [fields & body]
  (html
   (doctype :html5)
   [:html
    [:head (:title fields)]
    [:body
     (include-js "js/angular.js")
     (include-js "js/app.js")
     body]]))

(defn task [{n :task/number desc :task/description}]
  [:div
   [:span n]
   [:span desc]])

(defn index [tasks]
  (master {:title "Yeah"}
          [:div {:ng-controller "TaskCtrl"}
            [:script ""]
            [:ul "Tasks" (map (fn [t] [:li (task t)]) tasks)]]))
