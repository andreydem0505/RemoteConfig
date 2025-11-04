import http from 'k6/http';
import { check, sleep } from 'k6';
import test_data from "./test_data.js";

export const options = {
    stages: [
        { duration: '1m', target: 20 },
        { duration: '1m', target: 20 },
        { duration: '1m', target: 50 },
        { duration: '1m', target: 50 },
        { duration: '1m', target: 100 },
        { duration: '1m', target: 100 },
        { duration: '1m', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(99)<100'],
        http_req_failed: ['rate<0.01'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

function register(username, password) {
    const payload = JSON.stringify({
        username: username,
        password: password
    });

    const response = http.post(
        `${BASE_URL}/auth/register`,
        payload,
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { name: 'Register' }
        }
    );

    check(response, {
        'registration status is 200': (r) => r.status === 200,
    });
    sleep(0.01);
}

function login(username, password) {
    const payload = JSON.stringify({
        username: username,
        password: password
    });

    const response = http.post(
        `${BASE_URL}/auth/login`,
        payload,
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { name: 'Login' }
        }
    );

    check(response, {
        'authentication status is 200': (r) => r.status === 200,
    });
    sleep(0.01);
    return response.body;
}

function createProperty(jwtToken, property) {
    const payload = JSON.stringify({
        name: property.name,
        type: property.type,
        data: property.data
    });

    const response = http.post(
        `${BASE_URL}/properties`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            tags: { name: 'Create property' }
        }
    );

    check(response, {
        'property creation status is 201': (r) => r.status === 201,
    });
    sleep(0.1);
}

function checkProperty(jwtToken, property) {
    if (property.type === 'CUSTOM_PROPERTY') {
        getCustomPropertyData(jwtToken, property.name);
    } else {
        checkFeatureFlagHit(jwtToken, property.name, property.context);
    }

    sleep(0.1);
}

function getCustomPropertyData(jwtToken, propertyName) {
    const response = http.get(
        `${BASE_URL}/properties/${propertyName}`,
        {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            tags: { name: 'Get custom property data' }
        }
    );

    check(response, {
        'property data accessing status is 200': (r) => r.status === 200,
    });
}

function checkFeatureFlagHit(jwtToken, propertyName, context) {
    const payload = context === undefined ? '' : JSON.stringify({
        context: context
    });
    const response = http.post(
        `${BASE_URL}/properties/check/${propertyName}`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            tags: { name: 'Check feature flag hit' }
        }
    );

    check(response, {
        'feature flag checking hit status is 200': (r) => r.status === 200,
    });
}

const usersNumber = 10;
let userTokens = Array(usersNumber).fill(null);

export default function () {
    const index = __VU % usersNumber;
    if (userTokens[index] === null) {
        const username = `username_${__VU}`;
        const password = `password_${__VU}`;

        register(username, password);
        userTokens[index] = login(username, password);
        for (const property of test_data) {
            createProperty(userTokens[index], property);
        }
    } else {
        const property = test_data[Math.floor(Math.random() * test_data.length)];
        checkProperty(userTokens[index], property);
    }
}