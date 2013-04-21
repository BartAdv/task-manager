(ns task-manager.server
  (:use compojure.core
        [ring.adapter.jetty :only (run-jetty)]
        [task-manager.tasks :as tasks :reload true])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :refer (wrap-json-body wrap-json-response)])
  (:gen-class :main true))

(defroutes app-routes
  (GET "/" [] (tasks/index))
  (GET "/bututak" [] "Bu-tu-tak!!!")
  (POST ["/task/:number/status/:status", :id #"[0-9]+"] [number status]
        (tasks/update-status (read-string number) status))
  (PUT "/task/:desc" [desc]
       (tasks/create desc))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

(defn run [options]
  (let [options (merge {:port 8080 :join? false} options)]
    (run-jetty (var app) options)))

(defn -main [& args]
  (run {}))
