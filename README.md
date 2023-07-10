# Order_Processor_App
Created a parallel order processor using Java threads, where every order has packages from different deposits.
Due to the large size of the packages, threads creating threads were needed, the ones that process the orders create tasks for processing the products, thus used Replicated Workers design pattern.
