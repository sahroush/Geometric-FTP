#pragma GCC optimize("O2")
#include <bits/stdc++.h>

using namespace std;

typedef long long ll;
typedef long double ld;
typedef pair<int, int> pii;

mt19937 rng(chrono::steady_clock::now().time_since_epoch().count());

const int maxn = 50;
const ll mod = 1e9 + 7;

#define pb push_back
#define endl '\n'
#define ms(x, y) memset(x, y, sizeof x)
ll pw(ll a, ll b, ll md = mod) { ll res = 1; while (b) { if (b & 1) { res = (a * res) % md; } a = (a * a) % md; b >>= 1; } return(res); }

struct point {
    vector<ld> coords; // Support for multiple dimensions
};

int n, dims;
point origin;
vector<point> p;
ld ans = 1e9;
int d[maxn];
int par[maxn];
int cnt = 0;
int sz[maxn];
int norm_type = 2; // Default to L2 (Euclidean)

int dsu[maxn];

int getpar(int v) {
    return ((dsu[v] != -1) ? getpar(dsu[v]) : v);
}

stack<pii> stk;

void merge(int u, int v) {
    u = getpar(u), v = getpar(v);
    if (sz[u] > sz[v])
        swap(u, v);
    dsu[u] = v;
    stk.push({ u, v });
    sz[v] += sz[u];
}

void cut() {
    auto [u, v] = stk.top();
    stk.pop();
    dsu[u] = -1;
    sz[v] -= sz[u];
}

// General distance function supporting L1 (Manhattan), L2 (Euclidean), and L∞ (Chebyshev) norms
ld dist(int i, int j) {
    ld dist = 0;
    if (norm_type == 1) { // L1 (Manhattan distance)
        for (int k = 0; k < dims; ++k) {
            dist += abs(p[i].coords[k] - p[j].coords[k]);
        }
    }
    else if (norm_type == 2) { // L2 (Euclidean distance)
        for (int k = 0; k < dims; ++k) {
            dist += (p[i].coords[k] - p[j].coords[k]) * (p[i].coords[k] - p[j].coords[k]);
        }
        dist = sqrt(dist);
    }
    else if (norm_type == 3) { // L∞ (Chebyshev distance)
        for (int k = 0; k < dims; ++k) {
            dist = max(dist, abs(p[i].coords[k] - p[j].coords[k]));
        }
    }
    return dist;
}

ld solve(int v = 0, ld h = 0) {
    ld res = h;
    for (int i = 1; i < n; i++) if (par[i] == v)
        res = max(res, solve(i, h + dist(v, i)));
    return res;
}

void make_points() {
    p.clear();
    origin.coords = vector<ld>(dims, 0); // Origin is the zero vector
    p.pb(origin);

    // Example points (you can modify them according to the number of dimensions)    
    p.pb({{-0.1,-0.81,-0.09}});
    p.pb({{0.1, -0.09, 0.81}});
    p.pb({{0, 0, -1}});
    p.pb({{-0.3, -0.35, -0.35}});
    p.pb({{0.3, 0.35, 0.35}});
    p.pb({{-0.9, -0.05, 0.05}});
    p.pb({{0.9, 0.03, 0.07}});
    p.pb({{0.1, 0.81, -0.09}});

    n = p.size();
    ms(dsu, -1);
    for (int i = 0; i < n; i++)
        sz[i] = 1;
}

void generate_all_trees(int v = 1) {
    if (v == n) {
        if (d[0] != 1) return;
        ans = min(ans, solve());
        return;
    }
    for (int i = 0; i < n; i++) {
        par[v] = i;
        d[i]++;
        if (d[i] <= 2 and getpar(i) != getpar(v)) {
            merge(v, i);
            generate_all_trees(v + 1);
            cut();
        }
        d[i]--;
    }
}

int32_t main() {
    // Input number of dimensions and the type of norm
    cout << "Enter the number of dimensions (first modify the code and add your points): ";
    cin >> dims;

    cout << "Enter the norm type (1 = L1, 2 = L2, 3 = L∞): ";
    cin >> norm_type;

    ans = 1e9;
    make_points();
    generate_all_trees();
    cout << "Max distance: " << ans << endl;

    return 0;
}