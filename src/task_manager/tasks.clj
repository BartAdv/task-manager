(ns task-manager.tasks
  (:use [compojure.core]
        [task-manager.data :reload true])
  (:require [task-manager.views :as views :reload true]))

(defn get-tasks [] 
  (sort-by :task/number (tasks)))

(defn index []
  (views/index (get-tasks)))

(defn details [number]
    (views/details (task number)))

(defn update-status [num status]
  (let [id (:db/id (task num))]
    (update-task id :task/status (symbol "task.status" status))
    (get-tasks)))

;; let's be picky about what we update
(defn update [{number :task/number status :task/status description :task/description}]
  (let [id (:db/id (task number))]
    (update-task id 
                 ;;:task/status status 
                 :task/description description))
  (get-tasks))

(defn create [desc]
  (do
    (create-task desc)
    (get-tasks)))
