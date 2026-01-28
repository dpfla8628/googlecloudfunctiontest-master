/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// [START functions_helloworld_get]

package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloWorld implements HttpFunction {
  // Simple function to return "Hello World"
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException {

      response.appendHeader("Access-Control-Allow-Origin", "*");

      if ("OPTIONS".equals(request.getMethod())) {
          response.appendHeader("Access-Control-Allow-Methods", "GET");
          response.appendHeader("Access-Control-Allow-Headers", "Content-Type");
          response.appendHeader("Access-Control-Max-Age", "3600");
          response.setStatusCode(HttpURLConnection.HTTP_NO_CONTENT);
          return;
      }

    HikariConfig config = new HikariConfig();
    String DB_NAME = "34.64.154.199:3306/testdb2";
         //jdbc:mysql://34.171.40.210:3306/guestbook
    String DB_USER = "root";
    String DB_PASS = "ahqlwps.12#";
    String INSTANCE_CONNECTION_NAME = "studied-temple-357602:asia-northeast3:testmysql";
// The following URL is equivalent to setting the config options below:
// jdbc:mysql:///<DB_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&
// socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<DB_USER>&password=<DB_PASS>
// See the link below for more info on building a JDBC URL for the Cloud SQL JDBC Socket Factory
// https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory#creating-the-jdbc-url

// Configure which instance and what database user to connect with.
//    config.setJdbcUrl(String.format("jdbc:mysql://%s", DB_NAME));
      config.setJdbcUrl("jdbc:mysql://34.64.154.199:3306/testdb2");
    config.setUsername(DB_USER); // e.g. "root", "mysql"
    config.setPassword(DB_PASS); // e.g. "my-password"

//    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
//    config.addDataSourceProperty("cloudSqlInstance", INSTANCE_CONNECTION_NAME);

// The ipTypes argument can be used to specify a comma delimited list of preferred IP types
// for connecting to a Cloud SQL instance. The argument ipTypes=PRIVATE will force the
// SocketFactory to connect with an instance's associated private IP.
//    config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");

// ... Specify additional connection properties here.
// ...

// Initialize the connection pool using the configuration object.
    DataSource pool = new HikariDataSource(config);
      String str = "";
      PreparedStatement pst = null;
      ResultSet rs = null;
    try{
      Connection con = pool.getConnection();
      pst = con.prepareStatement("SELECT Column1 FROM NewTable");
      rs = pst.executeQuery();
          while (rs.next()) {
              str += rs.getString("Column1") + "\n";
          }
      }catch (Exception e) {
e.printStackTrace();
        }

    BufferedWriter writer = response.getWriter();
    writer.write(str);
  }
}
// [END functions_helloworld_get]
