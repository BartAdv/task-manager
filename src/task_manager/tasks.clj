(ns task-manager.tasks
  (:use [compojure.core])
  (:require [task-manager.views :as views :reload true]
            [task-manager.data :as data :reload true]))

(defn index []
  (let [tasks (data/tasks)]
    (views/index tasks)))

(defn save-task! [num desc]
  (do
    (data/task! num desc)
    (views/index (data/tasks))))

(defn create-task! [desc]
  (do
    (data/create-task! desc)
    (views/index (data/tasks))))
