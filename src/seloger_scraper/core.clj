(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [clojure.tools.cli :refer [parse-opts]]
   [seloger-scraper.listings :as listings])
  (:gen-class))

(def cli-options
  [["-c" "--city" "Name of the city you want to search accommodation in."
    :required true
    :default "montpellier"
    :parse-fn str
    :validate [listings/has-city? (str "Must be one of these cities: " (string/join "," (listings/get-cities)))]]
   ["-m" "--max-price" "The Maximum selling price."
    :required true
    :default 50000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be greater than zero."]]
   ["-h" "--help"]])

(defn -main
  [& args]
  (let [{{city :city max-price :max-price help :help} :options errors :errors} (parse-opts args cli-options)]
    (cond
      errors (println errors)
      help (println (string/join "\n" (cons
                                       "Scrape accomodation listings on seloger.com"
                                       (map #(->> %
                                                  (take 3)
                                                  (string/join " ")) cli-options))))
      :else (listings/search city max-price))
    (System/exit 0)))
