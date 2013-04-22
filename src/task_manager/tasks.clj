(ns task-manager.tasks
  (:use [compojure.core]
        [task-manager.data :reload true])
  (:require [task-manager.views :as views :reload true]))

(defn task->json [{n      :task/number 
                   desc   :task/description
                   status :task/status}]
  (hash-map :number n :description desc :status (last (re-find #"/(.+)" (str status)))))

(defn get-tasks [] 
  (->>
    (sort-by :task/number (tasks))
    (map task->json)))

(defn index []
  (views/index (get-tasks)))

(defn details [number]
    (views/details (task->json (task number))))

(defn update-status [num status]
  (let [id (:db/id (task num))]
    (update-task id :task/status (symbol "task.status" status))
    (get-tasks)))

;; let's be picky about what we update
(defn update [{number :number status :status description :description}]
  (let [id (:db/id (task number))]
    (update-task id 
                 :task/status (symbol "task.status" status)
                 :task/description description))
  (get-tasks))

(defn create [desc]
  (do
    (create-task desc)
    (get-tasks)))
