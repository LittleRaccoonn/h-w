package com.example.demo1;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<MonthSales> sales = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        Button btn = new Button("Upload file");
        root.getChildren().add(btn);

        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("месяц");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("общее кол ве за этот месяц");
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("График продаж товаров");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        root.getChildren().add(lineChart);

        btn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    readExcelFile(selectedFile);

                    series.getData().clear();


                    for (MonthSales item : sales) {
                        series.getData().add(new XYChart.Data<>(item.getMonths(), item.getSum()));
                    }

                    lineChart.getData().clear();
                    lineChart.getData().add(series);

                } catch (IOException | InvalidFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Продажи");
        stage.setScene(scene);
        stage.show();
    }

    public static void readExcelFile(File selectedFile) throws IOException, InvalidFormatException {
        FileInputStream fis = new FileInputStream(selectedFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        products.clear();
        sales.clear();

        // Хранение прибыли по месяцам
        Map<String, Double> monthlyProfits = new HashMap<>();

        // Используем DateTimeFormatter для формата dd.MM.yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Пропускаем заголовок

            // Проверка на null для каждой ячейки
            Cell idCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            int id = (int) idCell.getNumericCellValue(); // Ячейка 0: ID
            String name = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue(); // Ячейка 1: Название
            double price = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue(); // Ячейка 2: Цена
            int quantity = (int) row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue(); // Ячейка 3: Количество
            double finalPrice = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue(); // Ячейка 4: Конечная цена

            String dateString = "";
            // Проверка типа ячейки для даты
            Cell dateCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (dateCell.getCellType() == CellType.STRING) {
                dateString = dateCell.getStringCellValue();
            } else if (dateCell.getCellType() == CellType.NUMERIC) {
                LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                dateString = date.format(formatter);
            }

            // Если строка даты пустая, пропустить эту запись
            if (dateString.isEmpty()) {
                continue;
            }

            // Преобразуем строку в LocalDate
            LocalDate date = LocalDate.parse(dateString, formatter);

            // Получаем название месяца
            String month = date.getMonth().toString(); // Используем название месяца (например, "JUNE")

            // Добавляем продукт в список
            products.add(new Product(id, name, price, quantity, finalPrice, month));

            // Группируем по месяцам
            monthlyProfits.put(month, monthlyProfits.getOrDefault(month, 0.0) + finalPrice);
        }

        workbook.close();

        // Заполнение списка с результатами
        for (Map.Entry<String, Double> entry : monthlyProfits.entrySet()) {
            sales.add(new MonthSales(entry.getKey(), entry.getValue()));
        }

        System.out.println("Загружено продуктов: " + products.size());
    }

}

