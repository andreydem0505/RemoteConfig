const test_data = [
    {
        type: 'CUSTOM_PROPERTY',
        name: 'site.welcome_message',
        data: 'Welcome to RemoteConfig! We hope you enjoy your stay.'
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'feature.new_ui_enabled',
        data: true
    },
    {
        type: 'PERCENTAGE_FEATURE_FLAG',
        name: 'feature.rollout_percentage',
        data: 25
    },
    {
        type: 'EQUALITY_FEATURE_FLAG',
        name: 'environment.default',
        data: 'staging',
        context: 'staging'
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'feature.allowed_regions',
        data: ['us-east', 'eu-central', 'ap-south'],
        context: 'us-east'
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'site.maintenance_mode',
        data: false
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'site.terms_version',
        data: {
            version: '1.4.2',
            releasedAt: '2025-08-01',
            notes: ['minor fixes', 'privacy update'],
            agreed: false
        }
    },
    {
        type: 'PERCENTAGE_FEATURE_FLAG',
        name: 'feature.checkout_timeout_seconds',
        data: 5
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'feature.beta_test_group',
        data: ['user:123', 'user:456'],
        context: 'user:123'
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'ui.items_per_page',
        data: { default: 20, max: 100 }
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'feature.enable_search_v2',
        data: true
    },
    {
        type: 'PERCENTAGE_FEATURE_FLAG',
        name: 'feature.ad_experiment_percentage',
        data: 15
    },
    {
        type: 'EQUALITY_FEATURE_FLAG',
        name: 'env.current',
        data: 'production',
        context: 'production'
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'feature.supported_languages',
        data: ['en', 'ru', 'de'],
        context: 'ru'
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'billing.currency',
        data: { default: 'USD', supported: ['USD', 'EUR', 'RUB'] }
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'feature.payments_enabled',
        data: true
    },
    {
        type: 'PERCENTAGE_FEATURE_FLAG',
        name: 'feature.free_trial_percentage',
        data: 30
    },
    {
        type: 'EQUALITY_FEATURE_FLAG',
        name: 'user.onboarding_flow',
        data: 'classic',
        context: 'classic'
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'feature.vip_access_list',
        data: ['vip:alice', 'vip:bob'],
        context: 'vip:alice'
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'notifications.email_enabled',
        data: true
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'notifications.daily_limit',
        data: { limit: 5, per: 'day', exceptions: ['admin@example.com'] }
    },
    {
        type: 'PERCENTAGE_FEATURE_FLAG',
        name: 'feature.dynamic_pricing_percentage',
        data: 40
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'auth.password_policy',
        data: {
            minLength: 8,
            requireNumbers: true,
            requireSpecial: false,
            blacklist: ['123456', 'password'],
            expiresDays: 180
        }
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'feature.logging_verbose',
        data: false
    },
    {
        type: 'EQUALITY_FEATURE_FLAG',
        name: 'experiment.homepage_variant',
        data: 'variant_b',
        context: 'variant_b'
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'feature.whitelisted_ips',
        data: ['192.168.1.10', '10.0.0.5'],
        context: '192.168.1.10'
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'search.max_results',
        data: 50
    },
    {
        type: 'BOOLEAN_FEATURE_FLAG',
        name: 'feature.image_optimization',
        data: true
    },
    {
        type: 'PERCENTAGE_FEATURE_FLAG',
        name: 'feature.api_rate_limit_percentage',
        data: 10
    },
    {
        type: 'EQUALITY_FEATURE_FLAG',
        name: 'ui.theme',
        data: 'dark',
        context: 'dark'
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'feature.country_rollout',
        data: ['US', 'CA', 'GB'],
        context: 'US'
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'service.timeouts',
        data: { connectMs: 1500, readMs: 5000, retries: [100, 200, 500] }
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'analytics.sample_rate',
        data: 0.125
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'ui.beta_features',
        data: ['dark_mode', 'compact_layout', { name: 'live_preview', enabled: true }]
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'pricing.tiers',
        data: {
            tiers: [
                { id: 'free', price: 0, limits: { projects: 1, storageGb: 1 } },
                { id: 'pro', price: 29, limits: { projects: 10, storageGb: 50 } },
                { id: 'enterprise', price: 199, limits: { projects: 1000, storageGb: 500 } }
            ],
            currency: 'USD'
        }
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'feature.release_windows',
        data: {
            windows: [
                { from: '09:00', to: '12:00' },
                { from: '14:00', to: '18:00' }
            ],
            timezone: 'Europe/Berlin'
        }
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'integrations.slack',
        data: { enabled: true, webhookUrl: 'https://hooks.slack.com/services/T/ABC/DEF' }
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'logging.sinks',
        data: [
            { name: 'file', level: 'INFO', path: '/var/log/rc/app.log' },
            { name: 'remote', level: 'ERROR', host: 'logs.example.com', port: 6514 }
        ]
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'recommendations.model_config',
        data: {
            model: 'collaborative_v2',
            version: '2025-10-01',
            thresholds: { minScore: 0.35, softCap: 0.85 }
        }
    },
    {
        type: 'EQUALITY_FEATURE_FLAG',
        name: 'auth.default_role',
        data: 'user',
        context: 'user'
    },
    {
        type: 'UNIT_IN_LIST_FEATURE_FLAG',
        name: 'experiment.early_access_users',
        data: ['alice', 'bob', 'carol'],
        context: 'alice'
    },
    {
        type: 'CUSTOM_PROPERTY',
        name: 'ux.tour_steps',
        data: [
            { id: 'welcome', title: 'Welcome', order: 1 },
            { id: 'features', title: 'Key features', order: 2 }
        ]
    }
];

export default test_data;
