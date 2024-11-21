import random
from locust import task, FastHttpUser, stats

stats.PERCENTILES_TO_CHART = [0.95, 0.99]


class CouponIssueV1(FastHttpUser):
  connection_timeout = 10.0
  network_timeout = 10.0

  @task
  def issue(self):
    payload = {
      "studentId": random.randint(1, 3000),
      "courseId": 1
    }
    with self.rest("POST", "/course-apply", json=payload):
      pass
