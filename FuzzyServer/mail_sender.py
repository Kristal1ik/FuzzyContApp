import smtplib
from email.mime.text import MIMEText
from email.utils import make_msgid
import requests
data = input()
subject = input()
email = input()
s = smtplib.SMTP('smtp.yandex.ru', 587)
s.ehlo()
s.starttls()
text = MIMEText(f"{data}", 'plain', "utf-8")
text['Message-ID'] =  make_msgid()
text['From'] = "fuzzymaxwell@yandex.ru"
text['To'] = email
text['Subject'] = subject
s.login("fuzzymaxwell@yandex.ru", "lcegflpdfhwreftr")
s.sendmail("fuzzymaxwell@yandex.ru", email, text.as_string())
s.quit()


if subject == "Fuzzy question"
    TOKEN = "6226590723:AAFC0wcUlNL7-dO8MH6PoCEPWqWbXI7ZRfM"
    chat_id = "976354774"
    message = "Привет. Тут письмо пришло.... Это всё из-за user-friendly... Но почту проверить стоит."
    url = f"https://api.telegram.org/bot{TOKEN}/sendMessage?chat_id={chat_id}&text={message}"
    print(requests.get(url).json())