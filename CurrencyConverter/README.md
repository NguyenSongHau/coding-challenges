# Currency Converter App
* Application structure: The application is built according to the MVC model, includes the following folders:
  * models: These models serve as data structures for managing currency details, latest rates, conversions, and historical data within the app.
  * views: Handles interfaces for adapters, dialogs, fragments, and activities
  * controller: This controller abstracts the networking logic, simplifying the view layer's access to currency data by providing clean methods for symbol retrieval, latest rates, and historical rates.
  * In addition, I have the following folders:
     * api: Contains the API endpoints. This API application supports communication with the Currency API, allowing retrieval of symbols, latest rates, and historical data for currency conversion.
     * utils: Contains common functions that are used repeatedly in building applications.
* How to run project
  * IDE: You should use Android Studio to run project.
  * SDK: The project has a minimum sdk of 24 and a maximum of 31. We recommend SDK 31 for the best performance of the application.
  * Virtual Machine: You should use some virtual machine have API 31 and above. You can use the virtual machine available in Android Studio or use Genymotion to emulate the virtual machine.
* Main functions of the Application:
  * Users are allowed to enter values ​​and select the currency they want to convert. In addition, after conversion, application will display conversions of other currencies.
  * The user enters the value, selects the currency to be converted and selects the time period. The application will display a line chart so that the user can easily track the conversion value fluctuations.
  * The application checks user input and also checks whether there is internet or not.
* Link video demo: https://drive.google.com/file/d/1OmLzzHUpvN097tXddOl6oSXPmq1O_sxs/view