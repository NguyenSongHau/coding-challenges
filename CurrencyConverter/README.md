# Currency Converter App
* Application structure: The application is built according to the MVC model, includes the following folders:
  * models: These models serve as data structures for managing currency details, latest rates, conversions, and historical data within the app.
  * views: Handles interfaces for adapters, dialogs, fragments, and activities
  * controller: This controller abstracts the networking logic, simplifying the view layer's access to currency data by providing clean methods for symbol retrieval, latest rates, and historical rates.
  * In addition, I have the following folders:
        * api: Contains the API endpoints. This API application supports communication with the Currency API, allowing retrieval of symbols, latest rates, and historical data for currency conversion.
        * utils: Contains common functions that are used repeatedly in building applications.
 