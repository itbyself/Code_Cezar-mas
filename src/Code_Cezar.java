import java.io.IOException; // Импорт класса для обработки исключений ввода-вывода
import java.nio.file.Files; // Импорт класса для работы с файлами
import java.nio.file.Path; // Импорт класса для представления пути к файлу
import java.nio.file.Paths; // Импорт класса для создания пути к файлу
import java.util.Scanner; // Импорт класса для считывания ввода пользователя

public class Code_Cezar {
    public static String encrypt(String text, int key) {
        StringBuilder encrypted = new StringBuilder(); // Создание объекта для хранения зашифрованного текста
        // Проход по каждому символу входного текста
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) { // Проверка, является ли символ буквой
                // Если символ — строчная буква
                if (ch >= 'a' && ch <= 'z') {
                    // Шифрование строчной буквы
                    encrypted.append((char) ((ch - 'a' + key) % 26 + 'a')); // Применение шифра
                } else if (ch >= 'A' && ch <= 'Z') { // Если символ — заглавная буква
                    // Шифрование заглавной буквы
                    encrypted.append((char) ((ch - 'A' + key) % 26 + 'A')); // Применение шифра
                }
            } else { // Если символ не буква
                encrypted.append(ch); // Добавляем его без изменений
            }
        }
        return encrypted.toString(); // Возвращаем зашифрованный текст
    }

    // Метод для расшифрования
    public static String decrypt(String text, int key) {
        return encrypt(text, 26 - key); // Расшифровка текста путем применения шифрования с обратным ключом
    }

    // Метод для чтения содержимого файла
    public static String readFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path)); // Чтение байтов из файла и преобразование их в строку
    }

    // Метод для записи содержимого в файл
    public static void writeFile(Path path, String content) throws IOException {
        Files.write(path, content.getBytes()); // Запись строки в файл как байты
    }

    // Метод для проверки допустимости ключа
    public static boolean isValidKey(int key) {
        return key >= 1 && key <= 25; // Проверка, находится ли ключ в допустимом диапазоне
    }

    // Метод для обработки пользовательского ввода
    public static void processInput(int mode, String filePath, int key) {
        Path path = Paths.get(filePath); // Преобразование строки пути в объект Path

        // Проверка, существует ли файл по указанному пути
        if (!Files.exists(path)) {
            System.out.println("Файл не существует. Пожалуйста, проверьте путь.");
            return;
        }

        String content; // Переменная для хранения содержимого файла
        try {
            content = readFile(path);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage()); // Обработка ошибки чтения
            return;
        }

        String result; // Переменная для хранения результата (зашифрованного или расшифрованного текста)
        String newFilePath; // Переменная для хранения нового пути к файлу

        // Проверка режима работы (шифрование или расшифрование)
        if (mode == 1) {
            result = encrypt(content, key); // Шифрование содержимого
            newFilePath = "encrypted_" + path.getFileName().toString(); // Создание имени нового файла
        } else if (mode == 2) {
            result = decrypt(content, key); // Расшифрование содержимого
            newFilePath = "decrypted_" + path.getFileName().toString(); // Создание имени нового файла
        } else {
            System.out.println("Неверный режим. Ожидался 1 (шифрование) или 2 (расшифрование)."); // Вывод сообщения об ошибке
            return; // Завершение метода
        }

        // Создание пути для выходного файла
        Path outputPath = path.getParent().resolve(newFilePath);
        try {
            writeFile(outputPath, result); // Запись результата в новый файл
            System.out.println("Результат сохранён в файл: " + outputPath); // Подтверждение успешного сохранения
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage()); // Обработка ошибки записи
        }
    }

    // Метод для брутфорс расшифрования файла
    public static void bruteForceDecrypt(String filePath) {
        Path path = Paths.get(filePath); // Преобразование строки пути в объект Path
        if (!Files.exists(path)) {
            System.out.println("Файл не существует. Пожалуйста, проверьте путь."); // Проверка существования файла
            return;
        }

        String content; // Переменная для хранения содержимого файла
        try {
            content = readFile(path); // Чтение содержимого файла
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage()); // Обработка ошибки чтения
            return;
        }

        // Цикл для перебора всех возможных ключей от 1 до 25
        for (int key = 1; key <= 25; key++) {
            String decrypted = decrypt(content, key); // Расшифрование содержимого с текущим ключом
            System.out.println("Ключ " + key + ": " + decrypted + " (" + decrypted.length() + " символов)"); // Вывод расшифрованного текста
        }
    }

    // Запуск программы
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для считывания ввода пользователя

        // Запрос режима работы
        System.out.println("Выберите режим: 1 - Шифрование, 2 - Расшифрование с ключом, 3 - Brute Force");
        int mode = scanner.nextInt(); // Чтение выбранного режима

        // Запрос пути к файлу
        System.out.print("Введите путь к файлу: ");
        scanner.nextLine(); // Считывание пустой строки после ввода числа
        String filePath = scanner.nextLine(); // Чтение пути к файлу

        // Проверка режима работы и запрос ключа для шифрования или расшифрования
        if (mode == 2 || mode == 1) {
            System.out.print("Введите ключ (1-25): "); // Запрос ключа
            int key = scanner.nextInt(); // Чтение ключа
            if (!isValidKey(key)) { // Проверка допустимости ключа
                System.out.println("Недопустимый ключ. Ключ должен быть в диапазоне от 1 до 25."); // Сообщение об ошибке
            } else {
                processInput(mode, filePath, key); // Обработка пользовательского ввода
            }
        } else if (mode == 3) { // Если выбран режим брутфорс
            bruteForceDecrypt(filePath); // Вызов метода для брутфорс-расшифрования
        } else {
            System.out.println("Некорректный режим."); // Сообщение об ошибке
        }
        scanner.close(); // Закрытие объекта Scanner
    }
}
