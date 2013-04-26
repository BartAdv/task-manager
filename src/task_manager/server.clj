(ns task-manager.server
  (:use compojure.core
        [ring.adapter.jetty :only (run-jetty)]
        [task-manager.tasks :as tasks :reload true])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :refer (wrap-json-body wrap-json-response)])
  (:gen-class :main true))

(defroutes app-routes
  ;; HTML
  (GET "/" [] (tasks/index))
  (GET "/bututak" [] "Bu-tu-tak!!!")
  ;; API
  (GET "/tasks" [] (tasks/all))
  (context ["/task/:number", :number #"[0-9]+"] [number]
           (let-routes [number (read-string number)]
                       (GET "/" []
                            (tasks/task number))
                       (POST "/comments/:text" [text]
                            (tasks/add-comment number text))
                       (POST "/status/:status" [status]
                             (tasks/update-status number status))))
  (POST "/task" {task :body}
        (tasks/update task))
  (POST "/task/:desc" [desc]
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
