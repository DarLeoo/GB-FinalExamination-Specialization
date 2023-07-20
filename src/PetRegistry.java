import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class PetRegistry {
    private static final String ANIMAL_FILE = "animals.txt";
    private static final String COMMANDS_FILE = "commands.txt";

    private Map<Integer, String> animals;
    private Map<Integer, String> commands;
    private Scanner scanner;

    public PetRegistry() {
        animals = new HashMap<>();
        commands = new HashMap<>();
        scanner = new Scanner(System.in);
        loadFromFile(ANIMAL_FILE, animals);
        loadFromFile(COMMANDS_FILE, commands);
    }

    public void run() {
        try (Counter counter = new Counter()) {
            while (true) {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addNewAnimal();
                        counter.add();
                        break;
                    case 2:
                        showCommands();
                        break;
                    case 3:
                        trainAnimal();
                        break;
                    case 4:
                        showAllAnimals();
                        break;
                    case 5:
                        showAllCommands();
                        break;
                    case 6:
                        System.out.println("Выход из программы.");
                        return;
                    default:
                        System.out.println("Некорректный выбор. Пожалуйста, повторите.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void showMenu() {
        System.out.println("Меню Реестра Домашних Животных:");
        System.out.println("1. Завести новое животное");
        System.out.println("2. Увидеть список команд животного");
        System.out.println("3. Обучить животное новым командам");
        System.out.println("4. Показать всех животных в реестре");
        System.out.println("5. Показать все команды в реестре");
        System.out.println("6. Выход");
        System.out.print("Введите ваш выбор: ");
    }

    private void addNewAnimal() {
        System.out.print("Введите имя животного: ");
        String name = scanner.nextLine();
        System.out.print("Введите класс животного: ");
        String animalClass = scanner.nextLine();

        int count = animals.size() + 1;
        animals.put(count, name + ", " + animalClass);
        saveToFile(ANIMAL_FILE, animals);
    }

    private void showCommands() {
        System.out.print("Введите ID животного: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Считываем символ новой строки

        String commands = this.commands.get(id);
        if (commands != null) {
            System.out.println("Команды для животного с ID " + id + ": " + commands);
        } else {
            System.out.println("Команды для животного с ID " + id + " не найдены.");
        }
    }

    private void trainAnimal() {
        System.out.print("Введите ID животного: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Считываем символ новой строки

        String animal = animals.get(id);
        if (animal != null) {
            String existingCommands = commands.get(id);
            System.out.print("Введите новые команды (разделенные пробелами): ");
            String newCommands = scanner.nextLine();
            if (existingCommands != null) {
                newCommands = existingCommands + " " + newCommands;
            }
            commands.put(id, newCommands);
            saveToFile(COMMANDS_FILE, commands);
            System.out.println("Животное с ID " + id + " обучено новым командам.");
        } else {
            System.out.println("Животное с ID " + id + " не найдено.");
        }
    }

    private void showAllAnimals() {
        System.out.println("Все животные в реестре:");
        for (Map.Entry<Integer, String> entry : animals.entrySet()) {
            System.out.println("ID " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private void showAllCommands() {
        System.out.println("Все команды животных в реестре:");
        for (Map.Entry<Integer, String> entry : commands.entrySet()) {
            System.out.println("ID " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private void saveToFile(String filename, Map<Integer, String> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Map.Entry<Integer, String> entry : data.entrySet()) {
                writer.println(entry.getKey() + ", " + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    private void loadFromFile(String filename, Map<Integer, String> data) {
        File file = new File(filename);
        if (!file.exists()) {
            return; // Если файл не существует, предполагаем, что реестр пустой.
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ", 2); // Разделяем только первое вхождение ", "
                int id = Integer.parseInt(parts[0]);
                String value = parts[1];
                data.put(id, value);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке из файла: " + e.getMessage());
        }
    }
    }
