(ns task-manager.server
  (:use compojure.core
        [ring.adapter.jetty :only (run-jetty)]
        [task-manager.tasks :reload true])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :refer (wrap-json-body wrap-json-response)]))

(defroutes app-routes
  (GET "/" [] (index))
  (GET "/bututak" [] "Bu-tu-tak!!!")
  (POST "/task" {{num :number desc :description} :body}
        (save-task num desc))
  (POST "/test" {body :body} (println body))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})))

(defn run [options]
  (let [options (merge {:port 8080 :join? false} options)]
    (run-jetty (var app) options)))
