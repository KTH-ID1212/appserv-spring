package se.kth.id1212.appserv.bank.repository;

/*
 * Leif Lindbäck 180918: Lots of small changes, but the actual script running
 * part is unchanged.
 */

/*
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class
 * from the iBATIS Apache project. Only removed dependency on Resource class
 * and a constructor
 * GPSHansl, 06.08.2015: regex for delimiter, rearrange comment/delimiter detection, remove some ide warnings.
 */

/*
 *  Copyright 2004 Clinton Begin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbUtil {

    private static final Pattern delimP = Pattern
        .compile("^\\s*(--)?\\s*delimiter\\s*=?\\s*([^\\s]+)+\\s*.*$",
                 Pattern.CASE_INSENSITIVE);
    private static final String DEFAULT_DELIMITER = ";";
    private final Connection connection;

    private final boolean autoCommit;

    private String delimiter = DEFAULT_DELIMITER;
    private boolean fullLineDelimiter = false;

    private DbUtil(Connection connection, boolean autoCommit) {
        this.connection = connection;
        this.autoCommit = autoCommit;
    }

    public static void emptyDb()
        throws SQLException, IOException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection con = DriverManager.getConnection(
            "jdbc:mariadb://localhost:3306/appservspringbank?serverTimezone" +
            "=UTC", "jpa", "jpa");
        DbUtil runner = new DbUtil(con, false);
        runner.runScript(new BufferedReader(new FileReader(
            "src/main/scripts/db/create-appservspringbank-mariadb.sql")));
    }

    private void setDelimiter(String delimiter, boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    private void runScript(Reader reader) throws IOException, SQLException {
        try {
            boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    connection.setAutoCommit(this.autoCommit);
                }
                runScript(connection, reader);
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runScript(Connection conn, Reader reader) throws SQLException {
        StringBuffer command = null;
        try {
            LineNumberReader lineReader = new LineNumberReader(reader);
            String line;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                final Matcher delimMatch = delimP.matcher(trimmedLine);
                if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                    // Do nothing
                } else if (delimMatch.matches()) {
                    setDelimiter(delimMatch.group(2), false);
                } else if (trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (trimmedLine.length() < 1 ||
                           trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (!fullLineDelimiter &&
                           trimmedLine.endsWith(getDelimiter()) ||
                           fullLineDelimiter &&
                           trimmedLine.equals(getDelimiter())) {
                    command.append(
                        line.substring(0, line.lastIndexOf(getDelimiter())));
                    command.append(" ");
                    this.execCommand(conn, command, lineReader);
                    command = null;
                } else {
                    command.append(line);
                    command.append("\n");
                }
            }
            if (command != null) {
                this.execCommand(conn, command, lineReader);
            }
            if (!autoCommit) {
                conn.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.rollback();
        }
    }

    private void execCommand(Connection conn, StringBuffer command,
                             LineNumberReader lineReader) throws SQLException {
        Statement statement = conn.createStatement();

        boolean hasResults = false;
        try {
            hasResults = statement.execute(command.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (autoCommit && !conn.getAutoCommit()) {
            conn.commit();
        }

        ResultSet rs = statement.getResultSet();
        if (hasResults && rs != null) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                String name = md.getColumnLabel(i);
            }
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String value = rs.getString(i);
                }
            }
        }

        try {
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDelimiter() {
        return delimiter;
    }
}