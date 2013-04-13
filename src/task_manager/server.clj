(ns task-manager.server
  (:use compojure.core
        [ring.adapter.jetty :only (run-jetty)]
        [task-manager.tasks :reload true])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (index))
  (route/resources "/")
  (route/not-found "Not Found"))

(defn run [options]
  (let [options (merge {:port 8080 :join? false} options)]
    (run-jetty (var app-routes) options)))
