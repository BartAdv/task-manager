(ns task-manager.tasks
  (:use [compojure.core]
        [task-manager.data :reload true])
  (:require [task-manager.views :as views :reload true]))

(defn task->json [{n      :task/number 
                   desc   :task/description
                   status :task/status}]
  (hash-map :number n :description desc :status (last (re-find #"/(.+)" (str status)))))

(defn index []
  (views/index (map task->json (tasks))))

(defn update-status [num status]
  (let [id (:db/id (task num))]
    (update-task id :task/status (symbol "task.status" status))
    (map task->json (tasks))))

(defn create [desc]
  (do
    (create-task desc)
    (map task->json (tasks))))
