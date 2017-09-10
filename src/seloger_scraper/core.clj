(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [clojure.tools.cli :refer [parse-opts]]
   [clojure.tools.logging :as log]
   [seloger-scraper.listings :as listings])
  (:gen-class))

(def cli-options
  [["-c" "--city" "Name of the city you want to search listings for."
    :required true
    :default "montpellier"
    :parse-fn str
    :validate [listings/has-city? (str "Must be one of these cities: " (string/join "," (listings/get-cities)))]]
   ["-m" "--max-price" "The Maximum price for the listings."
    :required true
    :default 50000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be greater than zero."]]])

(defn -main
  [& args]
  (let [{{city :city max-price :max-price} :options errors :errors} (parse-opts args cli-options)]
    (if errors
      (log/info errors)
      (listings/search city max-price))
    (System/exit 0)))
