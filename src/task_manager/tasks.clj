(ns task-manager.tasks
  (:use [compojure.core])
  (:require [task-manager.views :as views :reload true]
            [task-manager.data :as data :reload true]))

(defn index []
  (let [tasks (data/tasks)]
    (views/index tasks)))

(defn save-task [num desc]
  (do
    (println num ", " desc)
    (data/task! num desc)
    (views/index (data/tasks))))
