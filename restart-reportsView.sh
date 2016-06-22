cd /home/certi/CustomReportsTelefonica/
pkill -f ReportView
java -Dfile.encoding=utf-8 -jar ReportViewTarget/ReportView-1.0-SNAPSHOT.jar server configRv.yml &
