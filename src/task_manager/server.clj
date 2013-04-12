(ns task-manager.server
  (:use compojure.core
        [ring.adapter.jetty :only (run-jetty)])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/resources "/")
  (route/not-found "Not Found"))

(defn run [options]
  (let [options (merge {:port 8080 :join? false} options)]
    (run-jetty (var app-routes) options)))
