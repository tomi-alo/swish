// k6 load test: GET /shots/{id} against the live API.
// Run the SAME script before and after Redis so the numbers are comparable.
//
//   k6 run -e BASE_URL=https://swish-alooa.duckdns.org -e SWISH_SECURITY_USERNAME=... -e SWISH_SECURITY_PASSWORD=... load/shots.js
//
// setup() creates 20 test shots (needs the API login), the main loop reads them,
// and teardown() deletes them again so the database ends where it started.

import http from 'k6/http';
import { check } from 'k6';
import encoding from 'k6/encoding';

// No default on purpose: you must say which server you are load testing.
const BASE = __ENV.BASE_URL;
if (!BASE) {
  throw new Error('Set BASE_URL, e.g. -e BASE_URL=https://swish-alooa.duckdns.org');
}
const API = `${BASE}/swish/api/v1/shots`;
const AUTH = {
  headers: {
    'Content-Type': 'application/json',
    Authorization: 'Basic ' + encoding.b64encode(`${__ENV.SWISH_SECURITY_USERNAME}:${__ENV.SWISH_SECURITY_PASSWORD}`),
  },
};

export const options = {
  vus: 10,
  duration: '30s',
  thresholds: {
    'http_req_failed{name:GET /shots/{id}}': ['rate<0.01'],
    'http_req_duration{name:GET /shots/{id}}': ['p(95)<500'],
  },
};

export function setup() {
  const ids = [];
  for (let i = 0; i < 20; i++) {
    const body = JSON.stringify({
      category: 'THREE',
      location: 'LEFT_CORNER',
      shotsTaken: 20,
      shotsMade: 13,
      notes: 'k6 seed',
    });
    const res = http.post(API, body, AUTH);
    if (res.status !== 201) {
      // k6 skips teardown() when setup() throws, so clean up what we already made
      for (const id of ids) {
        http.del(`${API}/${id}`, null, AUTH);
      }
      throw new Error(`seeding failed: ${res.status} ${res.body}`);
    }
    ids.push(res.json('id'));
  }
  return { ids };
}

export default function (data) {
  const id = data.ids[Math.floor(Math.random() * data.ids.length)];
  const res = http.get(`${API}/${id}`, { tags: { name: 'GET /shots/{id}' } });
  check(res, { 'status is 200': (r) => r.status === 200 });
}

export function teardown(data) {
  for (const id of data.ids) {
    http.del(`${API}/${id}`, null, AUTH);
  }
}
