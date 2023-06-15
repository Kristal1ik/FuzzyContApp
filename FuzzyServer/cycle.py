import random

from fuzzy_new import Rules, Controller, Trapezoid, area
import matplotlib.pyplot as plt

g = 9.8
m = 0.5
maxis = 0.01
r = 0.002
R = 0.03
dt = 0.01
T = 60
x0 = 0.05
v0 = 0
l = 0.5
F1 = -20
F2 = 20
k = 0.1
e = 0.000001
solution = [random.uniform(-10,10) for i in  range(60)]
temp = []
for i in range(len(solution)//4):
    temp.append(solution[i * 4: i * 4 + 4])
print(temp)
# Физическое поведение маятника
def f(x, v, w):
    a = (m * r * r * (g - w)) / (0.5 * (m * R * R + maxis * r * r) + (m + maxis) * r * r)
    if (x == R and v < 0) or (x == l and v > 0):
        v = -v * (1 - k)
    xnew = x + v * dt + 0.5 * dt ** 2 * a
    vnew = v + a * dt
    if xnew > l:
        xnew = l
    if xnew < R:
        xnew = R
    # print(f"xnew = {xnew} vnew = {vnew}")

    return xnew, vnew


data_x = [x0]
data_t = [0]
data_v = [v0]
data_w = [0]
if __name__ == '__main__':
    namex = input()
    namev = input()
    namew = input()
    lst = []
    x, v = x0, v0
    for i in range(0, int(T / dt)):
        lst = []
        trap = Controller(x, v).return_()
        trunc1 = Trapezoid([Rules.w1[0], Rules.w1[1], Rules.w1[2], Rules.w1[3], trap[0]])
        trunc2 = Trapezoid([Rules.w2[0], Rules.w2[1], Rules.w2[2], Rules.w2[3], trap[1]])
        trunc3 = Trapezoid([Rules.w3[0], Rules.w3[1], Rules.w3[2], Rules.w3[3], trap[2]])
        trunc4 = Trapezoid([Rules.w4[0], Rules.w4[1], Rules.w4[2], Rules.w4[3], trap[3]])
        trunc5 = Trapezoid([Rules.w5[0], Rules.w5[1], Rules.w5[2], Rules.w5[3], trap[4]])
        lst.append(trunc1)
        lst.append(trunc2)
        lst.append(trunc3)
        lst.append(trunc4)
        lst.append(trunc5)
        w = area(lst)
        x, v = f(x, v, w)
        # x, v = f(x, v, 0)
        data_x.append(x)
        data_t.append(i)
        data_v.append(v)
        data_w.append(w)

    fig, ax = plt.subplots()

    ax.plot(data_t, data_x)
    ax.scatter(0, l, alpha=0)
    ax.scatter(0, 0, alpha=0)
    ax.set_xlabel("t, s")
    ax.set_ylabel("x, m")
    plt.savefig(namex)
    ax.clear()

    ax.plot(data_t, data_v)
    ax.scatter(0, l, alpha=0)
    ax.scatter(0, 0, alpha=0)
    ax.set_xlabel("t, s")
    ax.set_ylabel("v, m/s")
    plt.savefig(namev)
    ax.clear()

    ax.plot(data_t, data_w)
    ax.scatter(0, l, alpha=0)
    ax.scatter(0, 0, alpha=0)
    ax.set_xlabel("t, s")
    ax.set_ylabel("w, m/s^2")
    plt.savefig(namew)
    ax.clear()

