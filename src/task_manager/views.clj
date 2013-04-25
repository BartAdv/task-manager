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
      (include-js "/js/angular-resource.js")
      (include-js "/js/app.js")
      (include-js "/js/controllers.js")
      (include-js "/js/services.js")
      (include-css "/css/style.css")
      body]]))

(defn index [tasks]
  (master {:title "Yeah" :html-attribs {:ng-app "task-manager"}}
          [:div {:ng-view ""}]))
